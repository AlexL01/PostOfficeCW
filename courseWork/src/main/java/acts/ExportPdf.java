package acts;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

class ExportPdf implements ActionListener {
	display display;
	MyTable table;
	private static final Logger log = LogManager.getLogger(ExportPdf.class);
	
	public ExportPdf(display _display) { display = _display; }
	@SuppressWarnings("static-access")
	@Override
	public void actionPerformed(ActionEvent e) {
		log.info("Start export table to pdf");
		table = display.tables[display.currentClass];
		try {
			var pw = new PdfWriter("out.pdf");
			var pd = new PdfDocument(pw, new DocumentProperties());
			var pf = PdfFontFactory.createFont(FontProgramFactory.createFont("src/main/resources/fonts/ARIALUNI.TTF"));
			pd.setCloseWriter(true);
			pd.setDefaultPageSize(PageSize.A4);
			
			pd.addNewPage(PageSize.A4);
			var pp = pd.getLastPage();
			var pc = new PdfCanvas(pp, true);
			
			var ps = pp.getPageSize();
			var downOffset = 20;
			var rightOffset = ps.getWidth() / table.getColumnCount();
			var startPos = ps.getTop() - downOffset - 30;
			
			pc.setFontAndSize(pf, 12);
			pc.moveText(2, startPos + 5);
			for(int i = 0; i < table.getColumnCount(); ++i) {
				pc.rectangle(0 + rightOffset * i, startPos, rightOffset, downOffset);
				pc.showText(display.Columns[display.currentClass][i]);
				pc.moveText(rightOffset, 0);
			}
			
			for(int row = 1; row <= table.countRow; ++row) {
				pc.moveText(-ps.getWidth(), -downOffset);
				for(int col = 0; col < table.getColumnCount(); ++col) {
					pc.rectangle(0 + rightOffset * col, startPos - downOffset * row, rightOffset, downOffset);
					pc.showText(table.getValueAt(row - 1, col).toString());
					pc.moveText(rightOffset, 0);
				}
			}
			pc.stroke();
			pd.close();
			
			JOptionPane.showMessageDialog(display.window, "Отчет сохранен в папке проекта", 
                    "Report.pdf", JOptionPane.INFORMATION_MESSAGE);


		} catch (Exception e1) { e1.printStackTrace(); }
		log.info("Finish export table to pdf");
    }
};