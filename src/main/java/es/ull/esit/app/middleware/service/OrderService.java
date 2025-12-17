package es.ull.esit.app.middleware.service;

import es.ull.esit.app.middleware.model.BillResult;

import java.io.FileNotFoundException;          
import java.io.IOException;                    
import java.io.PrintWriter;                   
import java.nio.charset.StandardCharsets;      
import java.nio.file.Files;                    
import java.nio.file.Path;                     

/**
 * @brief Service that handles order-related calculations and receipt generation.
 *
 *        Provides methods to compute totals (including VAT) and to generate
 *        local text files representing printed receipts.
 */
public class OrderService {

  /** VAT rate applied to the subtotal (15%). */
  private static final double VAT_RATE = 0.15;

  public BillResult calculateBill(double itemsTotalSum) {
    double vat = itemsTotalSum * VAT_RATE;
    double total = itemsTotalSum + vat;

    return new BillResult(
        Math.round(itemsTotalSum * 100.0) / 100.0,
        Math.round(vat * 100.0) / 100.0,
        Math.round(total * 100.0) / 100.0);
  }

  /**
   * @brief Generates a local text file representing a receipt.
   *
   * @param receiptNo [int] Numeric identifier of the receipt.
   * @param bill      [BillResult] Calculated bill result to print.
   * @throws FileNotFoundException If the receipt file cannot be created or opened.
   */
  public void generateReceiptFile(int receiptNo, BillResult bill) throws FileNotFoundException {

    try {
      Files.createDirectories(Path.of("receipts"));

      Path file = Path.of("receipts", "billNo." + receiptNo + ".txt");
      try (PrintWriter output =
          new PrintWriter(Files.newBufferedWriter(file, StandardCharsets.UTF_8))) {

        output.println(" Bill number is: " + receiptNo);
        output.println("==============");
        output.println("--------------");
        output.println("Subtotal is: " + bill.getSubTotal() + " SR");
        output.println("vat: " + bill.getVat() + " SR");
        output.println("Total is: " + bill.getTotal() + " SR");
        output.println();
        output.println("THANK YOU FOR ORDERING");
      }

    } catch (IOException e) {
      FileNotFoundException fnfe = new FileNotFoundException(
          "Could not create/write receipt file for receiptNo=" + receiptNo);
      fnfe.initCause(e);
      throw fnfe;
    }
  }
}
