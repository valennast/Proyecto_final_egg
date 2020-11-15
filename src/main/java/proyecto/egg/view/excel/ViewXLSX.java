package proyecto.egg.view.excel;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;


import proyecto.egg.Entidades.Usuario;

@Component("ver.xlsx")
public class ViewXLSX extends AbstractXlsxView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		response.setHeader("Content-Disposition", "attachment; filename=\"Usuario_view.xlsx\"");
		
		Usuario usuario = (Usuario)model.get("usuario");
		
		Sheet sheet= workbook.createSheet("info_usuario");
		
		Row row= sheet.createRow(0);
		
		Cell cell= row.createCell(0);
		
		
		
		
		cell.setCellValue("Datos del cliente");
		
		row= sheet.createRow(1);
		cell=row.createCell(0);
		cell.setCellValue(usuario.getNombre());
		
		row= sheet.createRow(2);
		cell=row.createCell(0);
		cell.setCellValue(usuario.getApellido());

		sheet.createRow(3).createCell(0).setCellValue(usuario.getMail());		
	}

}
