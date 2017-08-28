package com.tikal.toledo.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.google.apphosting.api.ApiProxy.OverQuotaException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.tikal.toledo.factura.Estatus;
import com.tikal.toledo.model.Factura;
import com.tikal.toledo.sat.cfd.Comprobante;
public class EmailSender {

	
	
	public EmailSender() {
	}

	public void enviaEmail(String emailReceptor, String nombreReceptor, String pass) throws UnsupportedEncodingException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		String mensaje = "Su nueva contraseña es: " + pass;

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("no.reply.fcon@gmail.com", "Password Reset"));
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

	
	public void enviaFactura(String emailReceptor, Factura factura,String text, String filename) throws MessagingException, DocumentException, MalformedURLException, IOException{
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		
//		try {
			Message msg = new MimeMessage(session);
			
			//append PDF
			Multipart mp = new MimeMultipart();
			MimeBodyPart mbp = new MimeBodyPart();
			mbp.setContent("<h1>Factura Electrónica</h1>","text/html");
			mp.addBodyPart(mbp);
			ByteArrayOutputStream os= new ByteArrayOutputStream();
			Comprobante cfdi = Util.unmarshallXML(factura.getCfdiXML());
			PDFFactura pdfFactura = new PDFFactura();
			PdfWriter writer= PdfWriter.getInstance(pdfFactura.getDocument(), os);
			pdfFactura.getDocument().open();
			if (factura.getEstatus().equals(Estatus.TIMBRADO)||factura.getEstatus().equals(Estatus.CANCELADO)){
				
				if (factura.getEstatus().equals(Estatus.CANCELADO)) {
					pdfFactura.getPieDePagina().setFechaCancel(factura.getFechaCancelacion());
					pdfFactura.getPieDePagina().setSelloCancel(factura.getSelloCancelacion());
					pdfFactura.construirPdfCancelado(cfdi, factura.getSelloDigital(), factura.getCodigoQR(),factura.getSelloCancelacion(),factura.getFechaCancelacion());
					pdfFactura.crearMarcaDeAgua("CANCELADO", writer);
				}else{
				pdfFactura.construirPdf(cfdi, factura.getSelloDigital(), factura.getCodigoQR(), factura.getEstatus(),1);
				}
			pdfFactura.getDocument().close();
			byte[] datap= os.toByteArray();
			MimeBodyPart attachmentp = new MimeBodyPart();
			InputStream attachmentDataStreamp = new ByteArrayInputStream(datap);
			attachmentp.setFileName(cfdi.getSerie()+cfdi.getFolio()+".pdf");
			attachmentp.setContent(attachmentDataStreamp, "application/pdf");
			mp.addBodyPart(attachmentp);
			
			//append XML file
			MimeBodyPart attachmentx= new MimeBodyPart();
			InputStream attachmentDataStreamx= new ByteArrayInputStream(factura.getCfdiXML().getBytes());
			attachmentx.setFileName(cfdi.getSerie()+cfdi.getFolio()+".xml");
			attachmentx.setContent(attachmentDataStreamx,"text/xml");
			mp.addBodyPart(attachmentx);
//			DataHandler handler;
//			mbp.setDataHandler(handler);
//			DataSource s= new DataSource();
			
//			
//			msg.setFrom(new InternetAddress("facturacion@tikal.mx", "Factura "+filename));
//			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(emailReceptor, ""));
//			msg.setSubject("Factura "+filename);
////			
//			msg.setText("correo de prueba");
			
			msg.setFrom(new InternetAddress("no.reply.fcon@gmail.com", "Tornillos"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(emailReceptor, "Cliente"));
			msg.setSubject("Factura "+factura.getUuid());
//			msg.setText("P");
			msg.setContent(mp);
			Transport.send(msg);
			}
//		} catch (AddressException e) {
//			e.printStackTrace();
//		} catch (MessagingException e) {
//			e.printStackTrace();
//		} catch (DocumentException e) {
//			e.printStackTrace();
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}catch(OverQuotaException e){
//			System.out.println("Se alcanzó");
//		}
		
	}
	
	public void mailprueba() throws UnsupportedEncodingException, MessagingException{
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		
		 Message msg = new MimeMessage(session);
		  msg.setFrom(new InternetAddress("no.reply.fcon@gmail.com", "Tornillos"));
		  msg.addRecipient(Message.RecipientType.TO,
		                   new InternetAddress("israel.vigueras.ico@gmail.com", "Mr. User"));
		  msg.setSubject("Factura electrónica");
		  msg.setText("");
		  Transport.send(msg);
	}
}
