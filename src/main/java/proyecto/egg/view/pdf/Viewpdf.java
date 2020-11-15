package proyecto.egg.view.pdf;

import java.awt.Color;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfCell;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import proyecto.egg.Entidades.Usuario;
import proyecto.egg.Repositorios.Usuariodao;

@Component("ver")
public class Viewpdf extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Usuario usuario = (Usuario) model.get("usuario");
		
		PdfPTable tabla1 = new PdfPTable(1);
		tabla1.setSpacingAfter(20);
		
		PdfPCell cell = null;
		
		cell= new PdfPCell( new Phrase("Datos del cliente"));
		cell.setBackgroundColor(new Color(184, 218, 255));
		cell.setPadding(8f);
		tabla1.addCell(cell);
		
		
		tabla1.addCell(usuario.getNombre());
		tabla1.addCell(usuario.getApellido());
		tabla1.addCell(usuario.getMail());
		
		document.add(tabla1);
		
	}

}
