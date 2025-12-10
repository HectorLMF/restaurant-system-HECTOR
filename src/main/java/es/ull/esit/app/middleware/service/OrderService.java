package es.ull.esit.app.middleware.service;

import es.ull.esit.app.middleware.model.BillResult;

/**
 * @brief Service that handles order-related calculations and receipt generation.
 *
 *        Provides methods to compute totals (including VAT) and to generate
 *        local text files representing printed receipts.
 */
public class OrderService {

  /** VAT rate applied to the subtotal (15%). */
  private static final double VAT_RATE = 0.15;

  /**
   * @brief Calculates subtotal, VAT and total for a given items sum.
   *
   *        The values are rounded to two decimal places.
   *
   * @param itemsTotalSum [double] Sum of item prices before VAT.
   * @return [BillResult] Container holding subtotal, VAT and total amounts.
   */
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
   *        The file is stored under a "receipts" directory created in the
   *        working folder. File name format is "billNo.<receiptNo>.txt".
   *
   * @param receiptNo [int] Numeric identifier of the receipt.
   * @param bill      [BillResult] Calculated bill result to print.
   * @throws java.io.FileNotFoundException If the receipt file cannot be created or opened.
   */
  public void generateReceiptFile(int receiptNo, BillResult bill) throws java.io.FileNotFoundException {
    // Ensure the directory exists.
    new java.io.File("receipts").mkdirs();

    try (java.io.PrintWriter output = new java.io.PrintWriter("receipts/billNo." + receiptNo + ".txt")) {
      output.println(" Bill number is: " + receiptNo);
      output.println("==============");
      output.println("--------------");
      output.println("Subtotal is: " + bill.getSubTotal() + " SR");
      output.println("vat: " + bill.getVat() + " SR");
      output.println("Total is: " + bill.getTotal() + " SR");
      output.println();
      output.println("THANK YOU FOR ORDERING");
    }
  }
}
