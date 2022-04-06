import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
/**
 * Represents an asset that the user have bought or sold after a transaction has been made. After a
 * transaction it will be compared with the elements in the list of assets objects, if asset exists
 * the quantity will be updated, otherwise will be added. You won't be able to sell more units than
 * you have.
 */
public class Asset {

  /* Todo - Design and Implement the Asset class as you see fit. Justify the design decisions that
  you make within the implementation */

  private String assetSymbol;
  private Double assetGty;
  public List<Batch> assetBatches;
  /**
   * Class Constructor
   *
   * @param assetSymbol asset symbol
   * @param assetGty    amount of units of the asset
   */
  public Asset(String assetSymbol, Double assetGty) {
    this.assetSymbol = assetSymbol;
    this.assetGty = assetGty;
    this.assetBatches = new ArrayList<>();
  }

  /**
   * Gets the asset symbol.
   */
  public String getAssetSymbol() {
    return assetSymbol;
  }

  /**
   * Gets the asset quantity.
   */
  public Double getAssetGty() {
    return assetGty;
  }

  /**
   * @param assetGty Quantity of the asset, if the asset exists we update it
   */
  public void setAssetGty(Double assetGty) {
    this.assetGty = assetGty;
  }

  /**
   * @param assetBatch element to be added to the assetBatches list on new transactions and sorts the list by price;
   */
  public void addAssetBatch(Batch assetBatch) {
    this.assetBatches.add(assetBatch);
    this.assetBatches.sort(Comparator.comparing(Batch::getBatchUnitCost));
  }

  /**
   * removes from the assetBatches list the first element sorted by price. when selling all elements from a batch;
   */
  public void removeAssetBatch() {
    this.assetBatches.remove(0);
    this.assetBatches.sort(Comparator.comparing(Batch::getBatchUnitCost));
  }

  /**
   * returns the first batch sorted by price.
   */
  public Batch getFirstBatch() {
    return this.assetBatches.get(0);
  }

  /**
   * @param updatedBatch updates the first element with the received parameter to make account for the amended quantity
   */
  public void updateFirstElement(Batch updatedBatch) {
    this.assetBatches.set(0, updatedBatch);
  }
}
