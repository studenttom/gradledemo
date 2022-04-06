public class Batch {
    private double batchQty;
    private final double batchUnitCost;

    public Batch(double batchQty, double batchUnitCost) {
        this.batchQty = batchQty;
        this.batchUnitCost = batchUnitCost;
    }

    public double getBatchQty() {
        return batchQty;
    }

    public double getBatchUnitCost() {
        return batchUnitCost;
    }

    public void amendBatchQty(double soldQty){
        this.batchQty -= soldQty;
    }

    @Override
    public String toString() {
        return "Batch{" +
                "batchQty=" + batchQty +
                ", batchUnitCost=" + batchUnitCost +
                '}';
    }
}

