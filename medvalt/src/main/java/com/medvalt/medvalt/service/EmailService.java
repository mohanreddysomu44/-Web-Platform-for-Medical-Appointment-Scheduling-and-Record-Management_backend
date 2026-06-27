package com.medvalt.medvalt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    /**
     * Sends a prescription notification email to the patient.
     * Silently skips if mail is not configured.
     */
    public void sendPrescriptionEmail(
            String patientEmail,
            String patientName,
            String doctorName,
            String diagnosis,
            String medicines,
            String instructions,
            String followUpDate
    ) {
        if (mailSender == null || patientEmail == null || patientEmail.isBlank()) return;
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(patientEmail);
            helper.setSubject("Your Prescription from Dr. " + doctorName + " — MedVault");
            helper.setText(buildHtml(patientName, doctorName, diagnosis, medicines, instructions, followUpDate), true);

            mailSender.send(message);
        } catch (Exception e) {
            // Log but don't crash — email is optional
            System.err.println("[EmailService] Failed to send prescription email: " + e.getMessage());
        }
    }

    private String buildHtml(String patientName, String doctorName,
                             String diagnosis, String medicines,
                             String instructions, String followUpDate) {
        String meds = medicines != null
                ? medicines.replace("\n", "<br/>")
                : "—";

        return "<!DOCTYPE html><html><body style='font-family:Arial,sans-serif;background:#f5f7ff;margin:0;padding:20px'>" +
                "<div style='max-width:560px;margin:0 auto;background:#fff;border-radius:16px;border:1px solid #e8ecf5;overflow:hidden'>" +

                // Header
                "<div style='background:linear-gradient(135deg,#4f6ef7,#7c3aed);padding:28px 32px'>" +
                "<h1 style='color:#fff;font-size:22px;margin:0;font-family:Georgia,serif'>MedVault</h1>" +
                "<p style='color:rgba(255,255,255,.75);font-size:13px;margin:4px 0 0'>Your Health, Secured</p>" +
                "</div>" +

                // Body
                "<div style='padding:28px 32px'>" +
                "<p style='color:#1a1f36;font-size:15px'>Dear <b>" + patientName + "</b>,</p>" +
                "<p style='color:#4a5278;font-size:14px;line-height:1.6'>" +
                "Dr. <b>" + doctorName + "</b> has written a prescription for you via MedVault. " +
                "Please find the details below.</p>" +

                // Diagnosis
                "<div style='background:#f0fdf4;border:1px solid #bbf7d0;border-radius:10px;padding:16px;margin:18px 0'>" +
                "<p style='color:#059669;font-size:11px;font-weight:700;text-transform:uppercase;letter-spacing:1px;margin:0 0 6px'>Diagnosis</p>" +
                "<p style='color:#1a1f36;font-size:15px;font-weight:600;margin:0'>🩺 " + (diagnosis != null ? diagnosis : "—") + "</p>" +
                "</div>" +

                // Medicines
                "<div style='background:#f8f9ff;border:1px solid #e8ecf5;border-radius:10px;padding:16px;margin:14px 0'>" +
                "<p style='color:#4f6ef7;font-size:11px;font-weight:700;text-transform:uppercase;letter-spacing:1px;margin:0 0 8px'>💊 Medicines &amp; Dosage</p>" +
                "<p style='color:#1a1f36;font-size:14px;line-height:1.7;margin:0'>" + meds + "</p>" +
                "</div>" +

                // Instructions
                (instructions != null && !instructions.isBlank()
                        ? "<div style='margin:14px 0'>" +
                        "<p style='color:#8892b0;font-size:11px;font-weight:700;text-transform:uppercase;letter-spacing:1px;margin:0 0 5px'>Instructions</p>" +
                        "<p style='color:#4a5278;font-size:14px;margin:0'>📋 " + instructions + "</p></div>"
                        : "") +

                // Follow-up
                (followUpDate != null && !followUpDate.isBlank()
                        ? "<div style='background:#fffbeb;border:1px solid #fcd34d;border-radius:10px;padding:12px 16px;margin:14px 0'>" +
                        "<p style='color:#d97706;font-size:13px;font-weight:600;margin:0'>📅 Follow-up Date: " + followUpDate + "</p></div>"
                        : "") +

                "<hr style='border:none;border-top:1px solid #e8ecf5;margin:24px 0'/>" +
                "<p style='color:#8892b0;font-size:12px;line-height:1.6;margin:0'>" +
                "This prescription was issued digitally via <b>MedVault</b>. " +
                "Please login to your MedVault account to view the full prescription details. " +
                "If you have any questions, please contact your doctor directly.</p>" +
                "</div>" +

                // Footer
                "<div style='background:#f5f7ff;padding:16px 32px;border-top:1px solid #e8ecf5'>" +
                "<p style='color:#b0b8d0;font-size:11px;margin:0;text-align:center'>© 2025 MedVault · Your Digital Health Portal</p>" +
                "</div>" +
                "</div></body></html>";
    }
}