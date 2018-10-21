/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package namlt.xml.asm.prj.crawler.model;

/**
 *
 * @author ADMIN
 */
public enum BookPropertiesEnum implements ValueProperties {
    TRANSLATOR("Dịch giả:"), AUTHOR("Tác giả:"), SIZE("Khổ sách:"),
    PAGE_NUMBER("Số trang:"), PRICE("Giá bán:"), ISBN("ISBN:"),
    DESCRIPTION("Giới thiệu tóm tắt tác phẩm:");

    private final String[] verifyKey;

    private BookPropertiesEnum(String... verifyKey) {
        this.verifyKey = verifyKey;
    }

    public String[] getVerifyKey() {
        return verifyKey;
    }

}
