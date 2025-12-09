package es.ull.esit.app.middleware.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @brief Client-side model representing a main course returned by the backend.
 *
 *        Describes a single dish in the main course category as exposed by the
 *        "/api/maincourses" endpoint.
 */
public class MainCourse {

  /** Unique identifier of the dish (JSON property "foodId"). */
  @JsonProperty("foodId")
  private Long foodId;

  /** Name of the dish (JSON property "itemFood"). */
  @JsonProperty("itemFood")
  private String itemFood;

  /** Price of the dish (JSON property "foodPrice"). */
  @JsonProperty("foodPrice")
  private Integer foodPrice;

  /**
   * Identifier of the receipt this dish belongs to (JSON property "receiptId").
   * Currently not provided by the backend.
   */
  @JsonProperty("receiptId")
  private Long receiptId;

  /**
   * @brief Default constructor required for JSON deserialization.
   */
  public MainCourse() {
  }

  /**
   * @brief Constructs a main course with all fields.
   *
   * @param foodId    [Long] Unique identifier of the dish.
   * @param itemFood  [String] Name of the dish.
   * @param foodPrice [Integer] Price of the dish.
   * @param receiptId [Long] Identifier of the related receipt (optional).
   */
  public MainCourse(Long foodId, String itemFood, Integer foodPrice, Long receiptId) {
    this.foodId = foodId;
    this.itemFood = itemFood;
    this.foodPrice = foodPrice;
    this.receiptId = receiptId;
  }

  /**
   * @brief Gets the dish identifier.
   *
   * @return [Long] Unique identifier of the dish.
   */
  public Long getFoodId() {
    return foodId;
  }

  /**
   * @brief Sets the dish identifier.
   *
   * @param foodId [Long] Unique identifier of the dish.
   */
  public void setFoodId(Long foodId) {
    this.foodId = foodId;
  }

  /**
   * @brief Gets the name of the dish.
   *
   * @return [String] Name of the dish.
   */
  public String getItemFood() {
    return itemFood;
  }

  /**
   * @brief Sets the name of the dish.
   *
   * @param itemFood [String] Name of the dish.
   */
  public void setItemFood(String itemFood) {
    this.itemFood = itemFood;
  }

  /**
   * @brief Gets the price of the dish.
   *
   * @return [Integer] Price of the dish.
   */
  public Integer getFoodPrice() {
    return foodPrice;
  }

  /**
   * @brief Sets the price of the dish.
   *
   * @param foodPrice [Integer] Price of the dish.
   */
  public void setFoodPrice(Integer foodPrice) {
    this.foodPrice = foodPrice;
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
