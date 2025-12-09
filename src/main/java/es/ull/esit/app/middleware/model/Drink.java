package es.ull.esit.app.middleware.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @brief Client-side model representing a drink returned by the backend API.
 *
 *        This class is used to deserialize and manipulate drink data obtained
 *        from the "/api/drinks" endpoint.
 */
public class Drink {

  /** Unique identifier of the drink (JSON property "drinksId"). */
  @JsonProperty("drinksId")
  private Long drinksId;

  /** Name of the drink item (JSON property "itemDrinks"). */
  @JsonProperty("itemDrinks")
  private String itemDrinks;

  /** Price of the drink (JSON property "drinksPrice"). */
  @JsonProperty("drinksPrice")
  private Integer drinksPrice;

  /**
   * Identifier of the receipt this drink belongs to (JSON property "receiptId").
   * Currently not provided by the backend.
   */
  @JsonProperty("receiptId")
  private Long receiptId;

  /**
   * @brief Default constructor required for JSON deserialization.
   */
  public Drink() {
  }

  /**
   * @brief Constructs a drink with all fields.
   *
   * @param drinksId    [Long] Unique identifier of the drink.
   * @param itemDrinks  [String] Name of the drink item.
   * @param drinksPrice [Integer] Price of the drink.
   * @param receiptId   [Long] Identifier of the related receipt (optional).
   */
  public Drink(Long drinksId, String itemDrinks, Integer drinksPrice, Long receiptId) {
    this.drinksId = drinksId;
    this.itemDrinks = itemDrinks;
    this.drinksPrice = drinksPrice;
    this.receiptId = receiptId;
  }

  /**
   * @brief Gets the drink identifier.
   *
   * @return [Long] Unique identifier of the drink.
   */
  public Long getDrinksId() {
    return drinksId;
  }

  /**
   * @brief Sets the drink identifier.
   *
   * @param drinksId [Long] Unique identifier of the drink.
   */
  public void setDrinksId(Long drinksId) {
    this.drinksId = drinksId;
  }

  /**
   * @brief Gets the drink item name.
   *
   * @return [String] Name of the drink item.
   */
  public String getItemDrinks() {
    return itemDrinks;
  }

  /**
   * @brief Sets the drink item name.
   *
   * @param itemDrinks [String] Name of the drink item.
   */
  public void setItemDrinks(String itemDrinks) {
    this.itemDrinks = itemDrinks;
  }

  /**
   * @brief Gets the drink price.
   *
   * @return [Integer] Price of the drink.
   */
  public Integer getDrinksPrice() {
    return drinksPrice;
  }

  /**
   * @brief Sets the drink price.
   *
   * @param drinksPrice [Integer] Price of the drink.
   */
  public void setDrinksPrice(Integer drinksPrice) {
    this.drinksPrice = drinksPrice;
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
