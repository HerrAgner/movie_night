package com.spring.demo.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SearchResult {


    private String searchText;
    @JsonProperty("Search")
    private List<MovieSearchResult> searchResult;
    @JsonProperty("totalResults")
    private int totalResults;
    @JsonProperty("Response")
    private Boolean response;

    public SearchResult() {
    }

    public List<MovieSearchResult> getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(List<MovieSearchResult> searchResult) {
        this.searchResult = searchResult;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public Boolean getResponse() {
        return response;
    }

    public void setResponse(Boolean response) {
        this.response = response;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}
