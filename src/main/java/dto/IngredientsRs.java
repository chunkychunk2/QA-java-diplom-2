package dto;

import java.util.List;

public class IngredientsRs extends POJO{

    private boolean success;

    private List<Data> data;

    @Override
    public String toString() {
        return "IngredientsRs{" +
                "success=" + success +
                ", data=" + data +
                '}';
    }
}
