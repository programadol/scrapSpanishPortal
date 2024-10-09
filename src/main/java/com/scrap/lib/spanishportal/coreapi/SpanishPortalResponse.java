package com.scrap.lib.spanishportal.coreapi;

import java.util.List;
import java.util.Map;

public class SpanishPortalResponse {
    private String result;
    private String message;
    private String code;
    private List<Object> fieldErrors;
    private List<Object> globalErrors;
    private Data data;
    private List<Object> markupResponseData;
    private Object dataLayerEvent;
    private List<Object> dataLayers;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Object> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<Object> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public List<Object> getGlobalErrors() {
        return globalErrors;
    }

    public void setGlobalErrors(List<Object> globalErrors) {
        this.globalErrors = globalErrors;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public List<Object> getMarkupResponseData() {
        return markupResponseData;
    }

    public void setMarkupResponseData(List<Object> markupResponseData) {
        this.markupResponseData = markupResponseData;
    }

    public Object getDataLayerEvent() {
        return dataLayerEvent;
    }

    public void setDataLayerEvent(Object dataLayerEvent) {
        this.dataLayerEvent = dataLayerEvent;
    }

    public List<Object> getDataLayers() {
        return dataLayers;
    }

    public void setDataLayers(List<Object> dataLayers) {
        this.dataLayers = dataLayers;
    }

    public static class Data {
        private String valueH1;
        private String description;
        private String mapSearchUrl;
        private String listingSearchUrl;
        private String pagetarget;
        private String searchTotalsUrl;
        private boolean searchWithoutFilters;
        private String listingTotalResults;
        private String listingPriceByArea;
        private boolean existAlert;
        private String savedSearchButtonText;
        private MapData map;
        private String saleListingSearchUrl;
        private String rentListingSearchUrl;
        private String newDevelopmentUrl;
        private String shareListingSearchUrl;
        private String saleListingSearchUrlMobile;
        private String rentListingSearchUrlMobile;
        private String newDevelopmentUrlMobile;
        private String shareListingSearchUrlMobile;
        private TypologiesUrls typologiesUrls;
        private int filterCount;

        public String getValueH1() {
            return valueH1;
        }

        public void setValueH1(String valueH1) {
            this.valueH1 = valueH1;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getMapSearchUrl() {
            return mapSearchUrl;
        }

        public void setMapSearchUrl(String mapSearchUrl) {
            this.mapSearchUrl = mapSearchUrl;
        }

        public String getListingSearchUrl() {
            return listingSearchUrl;
        }

        public void setListingSearchUrl(String listingSearchUrl) {
            this.listingSearchUrl = listingSearchUrl;
        }

        public String getPagetarget() {
            return pagetarget;
        }

        public void setPagetarget(String pagetarget) {
            this.pagetarget = pagetarget;
        }

        public String getSearchTotalsUrl() {
            return searchTotalsUrl;
        }

        public void setSearchTotalsUrl(String searchTotalsUrl) {
            this.searchTotalsUrl = searchTotalsUrl;
        }

        public boolean isSearchWithoutFilters() {
            return searchWithoutFilters;
        }

        public void setSearchWithoutFilters(boolean searchWithoutFilters) {
            this.searchWithoutFilters = searchWithoutFilters;
        }

        public String getListingTotalResults() {
            return listingTotalResults;
        }

        public void setListingTotalResults(String listingTotalResults) {
            this.listingTotalResults = listingTotalResults;
        }

        public String getListingPriceByArea() {
            return listingPriceByArea;
        }

        public void setListingPriceByArea(String listingPriceByArea) {
            this.listingPriceByArea = listingPriceByArea;
        }

        public boolean isExistAlert() {
            return existAlert;
        }

        public void setExistAlert(boolean existAlert) {
            this.existAlert = existAlert;
        }

        public String getSavedSearchButtonText() {
            return savedSearchButtonText;
        }

        public void setSavedSearchButtonText(String savedSearchButtonText) {
            this.savedSearchButtonText = savedSearchButtonText;
        }

        public MapData getMap() {
            return map;
        }

        public void setMap(MapData map) {
            this.map = map;
        }

        public String getSaleListingSearchUrl() {
            return saleListingSearchUrl;
        }

        public void setSaleListingSearchUrl(String saleListingSearchUrl) {
            this.saleListingSearchUrl = saleListingSearchUrl;
        }

        public String getRentListingSearchUrl() {
            return rentListingSearchUrl;
        }

        public void setRentListingSearchUrl(String rentListingSearchUrl) {
            this.rentListingSearchUrl = rentListingSearchUrl;
        }

        public String getNewDevelopmentUrl() {
            return newDevelopmentUrl;
        }

        public void setNewDevelopmentUrl(String newDevelopmentUrl) {
            this.newDevelopmentUrl = newDevelopmentUrl;
        }

        public String getShareListingSearchUrl() {
            return shareListingSearchUrl;
        }

        public void setShareListingSearchUrl(String shareListingSearchUrl) {
            this.shareListingSearchUrl = shareListingSearchUrl;
        }

        public String getSaleListingSearchUrlMobile() {
            return saleListingSearchUrlMobile;
        }

        public void setSaleListingSearchUrlMobile(String saleListingSearchUrlMobile) {
            this.saleListingSearchUrlMobile = saleListingSearchUrlMobile;
        }

        public String getRentListingSearchUrlMobile() {
            return rentListingSearchUrlMobile;
        }

        public void setRentListingSearchUrlMobile(String rentListingSearchUrlMobile) {
            this.rentListingSearchUrlMobile = rentListingSearchUrlMobile;
        }

        public String getNewDevelopmentUrlMobile() {
            return newDevelopmentUrlMobile;
        }

        public void setNewDevelopmentUrlMobile(String newDevelopmentUrlMobile) {
            this.newDevelopmentUrlMobile = newDevelopmentUrlMobile;
        }

        public String getShareListingSearchUrlMobile() {
            return shareListingSearchUrlMobile;
        }

        public void setShareListingSearchUrlMobile(String shareListingSearchUrlMobile) {
            this.shareListingSearchUrlMobile = shareListingSearchUrlMobile;
        }

        public TypologiesUrls getTypologiesUrls() {
            return typologiesUrls;
        }

        public void setTypologiesUrls(TypologiesUrls typologiesUrls) {
            this.typologiesUrls = typologiesUrls;
        }

        public int getFilterCount() {
            return filterCount;
        }

        public void setFilterCount(int filterCount) {
            this.filterCount = filterCount;
        }

        public static class MapData {
            private double centreLat;
            private double centreLong;
            private int height;
            private int width;
            private double northEastLat;
            private double northEastLong;
            private double southWestLat;
            private double southWestLong;
            private int zoomLevel;
            private List<MapItem> items;

            public double getCentreLat() {
                return centreLat;
            }

            public void setCentreLat(double centreLat) {
                this.centreLat = centreLat;
            }

            public double getCentreLong() {
                return centreLong;
            }

            public void setCentreLong(double centreLong) {
                this.centreLong = centreLong;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public double getNorthEastLat() {
                return northEastLat;
            }

            public void setNorthEastLat(double northEastLat) {
                this.northEastLat = northEastLat;
            }

            public double getNorthEastLong() {
                return northEastLong;
            }

            public void setNorthEastLong(double northEastLong) {
                this.northEastLong = northEastLong;
            }

            public double getSouthWestLat() {
                return southWestLat;
            }

            public void setSouthWestLat(double southWestLat) {
                this.southWestLat = southWestLat;
            }

            public double getSouthWestLong() {
                return southWestLong;
            }

            public void setSouthWestLong(double southWestLong) {
                this.southWestLong = southWestLong;
            }

            public int getZoomLevel() {
                return zoomLevel;
            }

            public void setZoomLevel(int zoomLevel) {
                this.zoomLevel = zoomLevel;
            }

            public List<MapItem> getItems() {
                return items;
            }

            public void setItems(List<MapItem> items) {
                this.items = items;
            }

            public static class MapItem {
                private List<Ad> ads;
                private double latitude;
                private double longitude;
                private int type;

                public List<Ad> getAds() {
                    return ads;
                }

                public void setAds(List<Ad> ads) {
                    this.ads = ads;
                }

                public double getLatitude() {
                    return latitude;
                }

                public void setLatitude(double latitude) {
                    this.latitude = latitude;
                }

                public double getLongitude() {
                    return longitude;
                }

                public void setLongitude(double longitude) {
                    this.longitude = longitude;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public static class Ad {
                    private long adId;
                    private boolean isFavourited;
                    private double price;
                    private String priceText;
                    private boolean isAuction;

                    public long getAdId() {
                        return adId;
                    }

                    public void setAdId(long adId) {
                        this.adId = adId;
                    }

                    public boolean isFavourited() {
                        return isFavourited;
                    }

                    public void setFavourited(boolean isFavourited) {
                        this.isFavourited = isFavourited;
                    }

                    public double getPrice() {
                        return price;
                    }

                    public void setPrice(double price) {
                        this.price = price;
                    }

                    public String getPriceText() {
                        return priceText;
                    }

                    public void setPriceText(String priceText) {
                        this.priceText = priceText;
                    }

                    public boolean isAuction() {
                        return isAuction;
                    }

                    public void setAuction(boolean isAuction) {
                        this.isAuction = isAuction;
                    }
                }
            }
        }

        public static class TypologiesUrls {
            private Map<String, String> urls;

            public Map<String, String> getUrls() {
                return urls;
            }

            public void setUrls(Map<String, String> urls) {
                this.urls = urls;
            }
        }
    }
}
