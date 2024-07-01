package dto;

import java.util.List;

public class OrdersRs extends POJO {

    private String name;

    private boolean success;

    private String message;

    private List<Orders> orders;

    private Order order;

    private Integer total;

    private Integer totalToday;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<Orders> getOrders() {
        return orders;
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getTotalToday() {
        return totalToday;
    }

    public void setTotalToday(Integer totalToday) {
        this.totalToday = totalToday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "OrdersRs{" +
                "name='" + name + '\'' +
                ", success=" + success +
                ", message='" + message + '\'' +
                ", orders=" + orders +
                ", order=" + order +
                ", total=" + total +
                ", totalToday=" + totalToday +
                '}';
    }
}
