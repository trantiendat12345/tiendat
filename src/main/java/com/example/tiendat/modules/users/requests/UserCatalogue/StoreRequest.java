package com.example.tiendat.modules.users.requests.UserCatalogue;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank; // dung de kiem tra xem co rong khong
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
public class StoreRequest {

    @NotBlank(message = "Ten nhom thanh vien khong duoc de trong")
    private String name;

    @NotNull(message = "Trang thai khong duoc de trong")
    @Min(value = 0, message = "Trang thai khong hop le")
    @Max(value = 2, message = "Trang thai khong hop le")
    private Integer publish;

    public String getEmail() {
        return name;
    }

    public void setEmail(String name) {
        this.name = name;
    }

    public Integer getPublish() {
        return publish;
    }

    public void setPassword(Integer publish) {
        this.publish = publish;
    }
    
}
