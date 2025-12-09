package es.ull.esit.app.middleware.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @brief Client-side model representing an appetizer received from the backend API.
 *
 *        It is used by the Swing application to deserialize JSON sent by
 *        the REST server for the "/api/appetizers" endpoint.
 */
public class Appetizer {

  /** Unique identifier of the appetizer (JSON property "appetizersId"). */
  @JsonProperty("appetizersId")
  private Long appetizersId;

  /** Name of the appetizer item (JSON property "itemAppetizers"). */
  @JsonProperty("itemAppetizers")
  private String itemAppetizers;

  /** Price of the appetizer (JSON property "appetizersPrice"). */
  @JsonProperty("appetizersPrice")
  private Integer appetizersPrice;

  /**
   * Identifier of the receipt this appetizer belongs to (JSON property
   * "receiptId"). Currently not populated by the backend.
   */
  @JsonProperty("receiptId")
  private Long receiptId;

  /**
   * @brief Default constructor required for JSON deserialization.
   */
  public Appetizer() {
  }

  /**
   * @brief Constructs an appetizer with all fields.
   *
   * @param appetizersId    [Long] Unique identifier of the appetizer.
   * @param itemAppetizers  [String] Name of the appetizer item.
   * @param appetizersPrice [Integer] Price of the appetizer.
   * @param receiptId       [Long] Identifier of the related receipt (optional).
   */
  public Appetizer(Long appetizersId, String itemAppetizers, Integer appetizersPrice, Long receiptId) {
    this.appetizersId = appetizersId;
    this.itemAppetizers = itemAppetizers;
    this.appetizersPrice = appetizersPrice;
    this.receiptId = receiptId;
  }

  /**
   * @brief Gets the appetizer identifier.
   *
   * @return [Long] Unique identifier of the appetizer.
   */
  public Long getAppetizersId() {
    return appetizersId;
  }

  /**
   * @brief Sets the appetizer identifier.
   *
   * @param appetizersId [Long] Unique identifier of the appetizer.
   */
  public void setAppetizersId(Long appetizersId) {
    this.appetizersId = appetizersId;
  }

  /**
   * @brief Gets the appetizer item name.
   *
   * @return [String] Name of the appetizer item.
   */
  public String getItemAppetizers() {
    return itemAppetizers;
  }

  /**
   * @brief Sets the appetizer item name.
   *
   * @param itemAppetizers [String] Name of the appetizer item.
   */
  public void setItemAppetizers(String itemAppetizers) {
    this.itemAppetizers = itemAppetizers;
  }

  /**
   * @brief Gets the appetizer price.
   *
   * @return [Integer] Price of the appetizer.
   */
  public Integer getAppetizersPrice() {
    return appetizersPrice;
  }

  /**
   * @brief Sets the appetizer price.
   *
   * @param appetizersPrice [Integer] Price of the appetizer.
   */
  public void setAppetizersPrice(Integer appetizersPrice) {
    this.appetizersPrice = appetizersPrice;
  }

  /**
   * @brief Gets the identifier of the related receipt.
   *
   * @return [Long] Identifier of the receipt or null if not set.
   */
  public Long getReceiptId() {
    return receiptId;
  }

  /**
   * @brief Sets the identifier of the related receipt.
   *
   * @param receiptId [Long] Identifier of the receipt.
   */
  public void setReceiptId(Long receiptId) {
    this.receiptId = receiptId;
  }
}
