package com.ai.paas.ipaas.base.dao.mapper.bo;

public class IpaasImageResource {
    private Integer id;

    private String serviceCode;

    private String imageRepository;

    private String imageCode;

    private String imageName;

    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode == null ? null : serviceCode.trim();
    }

    public String getImageRepository() {
        return imageRepository;
    }

    public void setImageRepository(String imageRepository) {
        this.imageRepository = imageRepository == null ? null : imageRepository.trim();
    }

    public String getImageCode() {
        return imageCode;
    }

    public void setImageCode(String imageCode) {
        this.imageCode = imageCode == null ? null : imageCode.trim();
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName == null ? null : imageName.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}