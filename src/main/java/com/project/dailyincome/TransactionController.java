package com.project.dailyincome;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
@Controller
public class TransactionController {
	
    @Autowired
    private TransactionService service;
    @Autowired
    private UserService userService;
    @GetMapping("/")
    public String home(
            @RequestParam(defaultValue = "0") int page,
            Model model,
            HttpSession session) {

        if(session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        String username =
                (String) session.getAttribute("user");

        User user =
                userService.findByUsername(username);

        Page<Transaction> transactionPage =
                service.getTransactionsByUser(
                        user,
                        PageRequest.of(page, 5));

        List<Transaction> transactions =
                transactionPage.getContent();

        double income = transactions.stream()
                .filter(t -> "Income".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double expense = transactions.stream()
                .filter(t -> "Expense".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double balance = income - expense;

        model.addAttribute("username", username);
        model.addAttribute("transactions", transactions);
        model.addAttribute("income", income);
        model.addAttribute("expense", expense);
        model.addAttribute("balance", balance);

        model.addAttribute("count",
                transactionPage.getTotalElements());

        model.addAttribute("currentPage", page);

        model.addAttribute("totalPages",
                transactionPage.getTotalPages());

        return "index";
    }

    @PostMapping("/save")
    public String save(Transaction transaction,
                       HttpSession session) {

        String username =
                (String) session.getAttribute("user");

        User user =
                userService.findByUsername(username);

        transaction.setUser(user);

        service.save(transaction);

        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/";
    }

    // EDIT PAGE
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {

        Transaction transaction = service.getById(id);

        model.addAttribute("transaction", transaction);

        return "edit";
    }

    // UPDATE DATA
    @PostMapping("/update")
    public String update(Transaction transaction,
                         HttpSession session) {

        String username =
                (String) session.getAttribute("user");

        User user =
                userService.findByUsername(username);

        transaction.setUser(user);

        service.save(transaction);

        return "redirect:/";
    }
    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response,HttpSession session) throws IOException {

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=transactions.xlsx");

        String username =
                (String) session.getAttribute("user");

        User user =
                userService.findByUsername(username);

        List<Transaction> transactions =
                service.getByUser(user);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Transactions");

        Row headerRow = sheet.createRow(0);

        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Description");
        headerRow.createCell(2).setCellValue("Amount");
        headerRow.createCell(3).setCellValue("Category");
        headerRow.createCell(4).setCellValue("Type");
        headerRow.createCell(5).setCellValue("Date");

        int rowNum = 1;

        for (Transaction t : transactions) {

            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(t.getId());
            row.createCell(1).setCellValue(t.getDescription());
            row.createCell(2).setCellValue(t.getAmount());
            row.createCell(3).setCellValue(t.getCategory());
            row.createCell(4).setCellValue(t.getType());
            row.createCell(5).setCellValue(t.getTransactionDate());
        }

        for (int i = 0; i < 6; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }
    @GetMapping("/export/pdf")
    public void exportPdf(HttpServletResponse response,HttpSession session) throws Exception {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=transactions.pdf");

        String username =
                (String) session.getAttribute("user");

        User user =
                userService.findByUsername(username);

        List<Transaction> transactions =
                service.getByUser(user);

        Document document = new Document();

        PdfWriter.getInstance(document,
                response.getOutputStream());

        document.open();

        Font titleFont =
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);

        Paragraph title =
                new Paragraph("Personal Budget Tracker Report", titleFont);

        title.setAlignment(Element.ALIGN_CENTER);

        document.add(title);

        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(6);

        table.setWidthPercentage(100);

        table.addCell("ID");
        table.addCell("Description");
        table.addCell("Amount");
        table.addCell("Category");
        table.addCell("Type");
        table.addCell("Date");

        for(Transaction t : transactions) {

            table.addCell(String.valueOf(t.getId()));
            table.addCell(t.getDescription());
            table.addCell(String.valueOf(t.getAmount()));
            table.addCell(t.getCategory());
            table.addCell(t.getType());
            table.addCell(String.valueOf(t.getTransactionDate()));
        }

        document.add(table);

        document.close();
    }
}