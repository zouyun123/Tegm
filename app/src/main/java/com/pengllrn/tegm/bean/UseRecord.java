package com.pengllrn.tegm.bean;

/**
 * @author Administrator
 * @version $Rev$
 * @des ${UTODO}
 * @updateAuthor ${Author}$
 * @updateDate2017/10/8.
 */

public class UseRecord {
    private String date;
    private Double total_time;

    public UseRecord(String date, Double total_time) {
        this.date = date;
        this.total_time = total_time;
    }

    public String getDate() {
        return date;
    }

    public Double getTotal_time() {
        return total_time;
    }
}
