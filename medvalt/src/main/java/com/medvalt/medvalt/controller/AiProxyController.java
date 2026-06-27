package com.medvalt.medvalt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class AiProxyController {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper  = new ObjectMapper();

    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestBody Map<String, Object> body) {

        System.out.println("[GeminiProxy] Request received");

        try {
            List<Map<String, Object>> frontendMessages =
                    (List<Map<String, Object>>) body.get("messages");

            if (frontendMessages == null || frontendMessages.isEmpty()) {
                return ResponseEntity.badRequest().body("{\"error\":\"No messages provided\"}");
            }

            // ── Build contents — only real user messages ──────────────────
            // NO system injection as first turn — use system_instruction instead
            List<Map<String, Object>> contents = new ArrayList<>();

            for (Map<String, Object> msg : frontendMessages) {
                String role    = String.valueOf(msg.get("role"));
                String content = String.valueOf(msg.get("content"));

                if (content == null || content.isBlank() || content.equals("null")) continue;

                String geminiRole = "assistant".equals(role) ? "model" : "user";
                contents.add(buildTurn(geminiRole, content));
            }

            if (contents.isEmpty()) {
                return ResponseEntity.badRequest().body("{\"error\":\"No valid messages\"}");
            }

            // ── System instruction (correct way for Gemini 2.5) ───────────
            Map<String, Object> siPart = new HashMap<>();
            siPart.put("text",
                    "You are Medi, an AI medical assistant for MedVault, " +
                            "a doctor appointment platform in India. " +
                            "Help with symptoms, suggest specialists, explain test results, give first-aid guidance. " +
                            "MedVault: 500+ doctors, fees Rs.300-1500, book in 60 seconds. Emergency: call 108. " +
                            "Rules: For emergencies say CALL 108 NOW. Never diagnose definitively. " +
                            "Always remind to consult a real doctor. Be concise under 120 words."
            );
            Map<String, Object> systemInstruction = new HashMap<>();
            systemInstruction.put("parts", Collections.singletonList(siPart));

            // ── Generation config ─────────────────────────────────────────
            Map<String, Object> genConfig = new HashMap<>();
            genConfig.put("maxOutputTokens", 400);
            genConfig.put("temperature", 0.7);

            // ── Final payload ─────────────────────────────────────────────
            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("system_instruction", systemInstruction);
            payload.put("contents",           contents);
            payload.put("generationConfig",   genConfig);

            String json = objectMapper.writeValueAsString(payload);
            System.out.println("[GeminiProxy] Payload: " + json);

            // ── Call Gemini 2.5 Flash ─────────────────────────────────────
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String url = "https://generativelanguage.googleapis.com/v1beta/models/" +
                    "gemini-2.5-flash:generateContent?key=AIzaSyBijWSkXdC7rZoYP5f61jDU_ouOjs82v4Q" ;

            ResponseEntity<String> resp = restTemplate.exchange(
                    url, HttpMethod.POST, new HttpEntity<>(json, headers), String.class
            );

            System.out.println("[GeminiProxy] Success: " + resp.getStatusCode());

            // ── Parse response ────────────────────────────────────────────
            Map<String, Object> geminiResp = objectMapper.readValue(resp.getBody(), Map.class);
            List<Map<String, Object>> candidates =
                    (List<Map<String, Object>>) geminiResp.get("candidates");

            if (candidates == null || candidates.isEmpty()) {
                return ResponseEntity.ok(wrapText("Sorry, no response generated. Please try again."));
            }

            Map<String, Object> contentMap =
                    (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts =
                    (List<Map<String, Object>>) contentMap.get("parts");
            String replyText = String.valueOf(parts.get(0).get("text"));

            System.out.println("[GeminiProxy] Reply: " +
                    replyText.substring(0, Math.min(80, replyText.length())));

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(wrapText(replyText));

        } catch (HttpClientErrorException e) {
            System.err.println("[GeminiProxy] HTTP " + e.getStatusCode());
            System.err.println("[GeminiProxy] Body: " + e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode())
                    .body("{\"error\":\"" + e.getResponseBodyAsString().replace("\"", "'") + "\"}");
        } catch (Exception e) {
            System.err.println("[GeminiProxy] Exception: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    private Map<String, Object> buildTurn(String role, String text) {
        Map<String, Object> part = new HashMap<>();
        part.put("text", text);
        Map<String, Object> turn = new HashMap<>();
        turn.put("role",  role);
        turn.put("parts", Collections.singletonList(part));
        return turn;
    }

    private String wrapText(String text) throws Exception {
        Map<String, Object> block = new LinkedHashMap<>();
        block.put("type", "text");
        block.put("text", text);
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("content", Collections.singletonList(block));
        return new ObjectMapper().writeValueAsString(resp);
    }
}