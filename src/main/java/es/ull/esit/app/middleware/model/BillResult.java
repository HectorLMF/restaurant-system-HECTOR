package es.ull.esit.app.middleware.model;

/**
 * @brief Data Transfer Object representing the result of a bill calculation.
 *
 *        It stores subtotal, VAT and total and is used exclusively on the client
 *        side by the OrderService.
 */
public class BillResult {

  /** Calculated subtotal amount before VAT. */
  private final double subTotal;

  /** Calculated VAT amount. */
  private final double vat;

  /** Final total amount including VAT. */
  private final double total;

  /**
   * @brief Constructs a bill result with the given amounts.
   *
   * @param subTotal [double] Subtotal amount before VAT.
   * @param vat      [double] VAT amount.
   * @param total    [double] Final total amount including VAT.
   */
  public BillResult(double subTotal, double vat, double total) {
    this.subTotal = subTotal;
    this.vat = vat;
    this.total = total;
  }

  /**
   * @brief Gets the subtotal amount.
   *
   * @return [double] Subtotal amount before VAT.
   */
  public double getSubTotal() {
    return subTotal;
  }

  /**
   * @brief Gets the VAT amount.
   *
   * @return [double] VAT amount.
   */
  public double getVat() {
    return vat;
  }

  /**
   * @brief Gets the total amount.
   *
   * @return [double] Final total amount including VAT.
   */
  public double getTotal() {
    return total;
  }
}
