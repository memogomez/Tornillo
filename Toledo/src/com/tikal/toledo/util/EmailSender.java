package com.tikal.toledo.util;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {

	public EmailSender() {

	}

	public void enviaEmail(String emailReceptor, String nombreReceptor, String pass) throws UnsupportedEncodingException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		String mensaje = "Su nueva contraseña es: " + pass;

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("ing.danielcastrog@gmail.com", "Password Reset"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(emailReceptor, nombreReceptor));
			msg.setSubject("Contraseña Nueva");
			msg.setText(mensaje);
			Transport.send(msg);

		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
