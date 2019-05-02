package sk.fri.uniza.api;

import java.util.Collection;

/**
 * This class is used as a container for paged data
 *
 * @param <T> Type of collection object that holds the paged data
 */
public class Paged<T extends Collection> {

    private int page = 1;
    private int size = 0;
    private long totalSize = 0;
    private int lastPage = 0;
    private Integer nextPage = null;
    private Integer prevPage = null;
    private T data;

    public Paged() {
    }

    public Paged(T data) {
        this.data = data;
    }

    public Paged(int page, int limit, long totalSize, T data) {
        this.page = page;
        this.size = data != null ? data.size() : 0;
        this.totalSize = totalSize;
        this.lastPage = (int) (Math.ceil((float) totalSize / limit));
        this.data = data;
        nextPage = page < lastPage ? page + 1 : null;
        prevPage = (page > 1) && (page <= lastPage) ? page - 1 : null;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }

    public Integer getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(Integer prevPage) {
        this.prevPage = prevPage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}
