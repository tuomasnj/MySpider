package com.seu.blog.vo.params;
import lombok.Data;

/**
 * view object 视图层，用于存放和前端交互的数据
 */
@Data
public class PageParams {
    private int page =1;
    private int pageSize=10;
    private Long categoryId;
    private Long tagId;

    private String year;

    private String month;
    public String getMonth(){
        if (this.month != null && this.month.length() == 1){
            return "0"+this.month;
        }
        return this.month;
    }
}
