package com.exzone.lib.widget.recycler.manager;

import android.content.Context;
import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.exzone.lib.R;
import com.exzone.lib.widget.recycler.stickyheaders.SectioningAdapter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * 作者:lhh
 * 描述:必须与关联使用SectioningAdapter
 * 时间:2016/7/11.
 */
public class StickyHeaderLayoutManager extends RecyclerView.LayoutManager {

  private static final String TAG = StickyHeaderLayoutManager.class.getSimpleName();
  SectioningAdapter adapter;
  // holds all the visible section headers
  HashSet<View> headerViews = new HashSet<>();
  // holds the HeaderPosition for each header
  HashMap<Integer, HeaderPosition> headerPositionsBySection = new HashMap<>();
  HeaderPositionChangedCallback headerPositionChangedCallback;
  // adapter position of first (lowest-y-value) visible item.
  int firstViewAdapterPosition;
  // top of first (lowest-y-value) visible item.
  int firstViewTop;
  // adapter position (iff >= 0) of the item selected in scrollToPosition
  int scrollTargetAdapterPosition = -1;
  SavedState pendingSavedState;

  public StickyHeaderLayoutManager() {
  }

  public HeaderPositionChangedCallback getHeaderPositionChangedCallback() {
    return headerPositionChangedCallback;
  }

  /**
   * Assign callback object to be notified when a header view position changes between states of the
   * HeaderPosition enum
   *
   * @param headerPositionChangedCallback the callback
   * @see HeaderPosition
   */
  public void setHeaderPositionChangedCallback(
      HeaderPositionChangedCallback headerPositionChangedCallback) {
    this.headerPositionChangedCallback = headerPositionChangedCallback;
  }

  @Override public void onAttachedToWindow(RecyclerView view) {
    super.onAttachedToWindow(view);
    try {
      adapter = (SectioningAdapter) view.getAdapter();
    } catch (ClassCastException e) {
      throw new ClassCastException(
          "StickyHeaderLayoutManager must be used with a RecyclerView where the adapter is a kind of SectioningAdapter");
    }
  }

  @Override
  public void onAdapterChanged(RecyclerView.Adapter oldAdapter, RecyclerView.Adapter newAdapter) {
    removeAllViews();
    headerViews.clear();
    headerPositionsBySection.clear();
  }

  @Override public void onDetachedFromWindow(RecyclerView view, RecyclerView.Recycler recycler) {
    super.onDetachedFromWindow(view, recycler);
    adapter = null;
  }

  @Override public Parcelable onSaveInstanceState() {
    if (pendingSavedState != null) {
      return pendingSavedState;
    }

    updateFirstAdapterPosition();
    SavedState state = new SavedState();
    state.firstViewAdapterPosition = firstViewAdapterPosition;
    state.firstViewTop = firstViewTop;

    return state;
  }

  @Override public void onRestoreInstanceState(Parcelable state) {
    if (state == null) {
      return;
    }

    if (state instanceof SavedState) {
      pendingSavedState = (SavedState) state;
      requestLayout();
    } else {
      Log.e(TAG, "onRestoreInstanceState: invalid saved state class, expected: "
          + SavedState.class.getCanonicalName()
          + " got: "
          + state.getClass().getCanonicalName());
    }
  }

  @Override public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {

    if (scrollTargetAdapterPosition >= 0) {
      firstViewAdapterPosition = scrollTargetAdapterPosition;
      firstViewTop = 0;
      scrollTargetAdapterPosition = RecyclerView.NO_POSITION; // we're done here
    } else if (pendingSavedState != null && pendingSavedState.isValid()) {
      firstViewAdapterPosition = pendingSavedState.firstViewAdapterPosition;
      firstViewTop = pendingSavedState.firstViewTop;
      pendingSavedState = null; // we're done with saved state now
    } else {
      updateFirstAdapterPosition();
    }

    int top = firstViewTop;

    // RESET
    headerViews.clear();
    headerPositionsBySection.clear();
    detachAndScrapAttachedViews(recycler);

    int height;
    int left = getPaddingLeft();
    int right = getWidth() - getPaddingRight();
    int parentBottom = getHeight() - getPaddingBottom();
    int totalVendedHeight = 0;

    // walk through adapter starting at firstViewAdapterPosition stacking each vended item
    for (int adapterPosition = firstViewAdapterPosition; adapterPosition < state.getItemCount();
        adapterPosition++) {

      View v = recycler.getViewForPosition(adapterPosition);
      addView(v);
      measureChildWithMargins(v, 0, 0);

      int itemViewType = getViewType(v);
      if (itemViewType == SectioningAdapter.TYPE_HEADER) {
        headerViews.add(v);

        // use the header's height
        height = getDecoratedMeasuredHeight(v);
        layoutDecorated(v, left, top, right, top + height);

        // we need to vend the ghost header and position/size it same as the actual header
        adapterPosition++;
        View ghostHeader = recycler.getViewForPosition(adapterPosition);
        addView(ghostHeader);
        layoutDecorated(ghostHeader, left, top, right, top + height);
      } else if (itemViewType == SectioningAdapter.TYPE_GHOST_HEADER) {

        // we need to back up and get the header for this ghostHeader
        View headerView = recycler.getViewForPosition(adapterPosition - 1);
        headerViews.add(headerView);
        addView(headerView);
        measureChildWithMargins(headerView, 0, 0);
        height = getDecoratedMeasuredHeight(headerView);

        layoutDecorated(headerView, left, top, right, top + height);
        layoutDecorated(v, left, top, right, top + height);
      } else {
        height = getDecoratedMeasuredHeight(v);
        layoutDecorated(v, left, top, right, top + height);
      }

      top += height;
      totalVendedHeight += height;

      // if the item we just laid out falls off the bottom of the view, we're done
      if (v.getBottom() >= parentBottom) {
        break;
      }
    }

    // determine if scrolling is necessary to fill viewport
    int innerHeight = getHeight() - (getPaddingTop() + getPaddingBottom());
    if (totalVendedHeight < innerHeight) {
      // note: we're passing null for RecyclerView.State - this is "safe"
      // only because we don't use it for scrolling negative dy
      scrollVerticallyBy(totalVendedHeight - innerHeight, recycler, null);
    } else {
      // no scroll correction necessary, so position headers
      updateHeaderPositions(recycler);
    }
  }

  /**
   * Get the header item for a given section, creating it if it's not already in the view hierarchy
   *
   * @param recycler the recycler
   * @param sectionIndex the index of the section for in question
   * @return the header, or null if the adapter specifies no header for the section
   */
  View createSectionHeaderIfNeeded(RecyclerView.Recycler recycler, int sectionIndex) {

    if (adapter.doesSectionHaveHeader(sectionIndex)) {

      // first, see if we've already got a header for this section
      for (int i = 0, n = getChildCount(); i < n; i++) {
        View view = getChildAt(i);
        if (getViewType(view) == SectioningAdapter.TYPE_HEADER
            && getViewSectionIndex(view) == sectionIndex) {
          return view;
        }
      }

      // looks like we need to create one
      int headerAdapterPosition = adapter.getAdapterPositionForSectionHeader(sectionIndex);
      View headerView = recycler.getViewForPosition(headerAdapterPosition);
      headerViews.add(headerView);
      addView(headerView);
      measureChildWithMargins(headerView, 0, 0);

      return headerView;
    }

    return null;
  }

  @Override
  public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {

    if (getChildCount() == 0) {
      return 0;
    }

    int scrolled = 0;
    int left = getPaddingLeft();
    int right = getWidth() - getPaddingRight();

    if (dy < 0) {

      // content moving downwards, so we're panning to top of list

      View topView = getTopmostChildView();
      while (scrolled > dy) {

        // get the topmost view
        int hangingTop = Math.max(-getDecoratedTop(topView), 0);
        int scrollBy = Math.min(scrolled - dy,
            hangingTop); // scrollBy is positive, causing content to move downwards

        scrolled -= scrollBy;
        offsetChildrenVertical(scrollBy);

        // vend next view above topView

        if (firstViewAdapterPosition > 0 && scrolled > dy) {
          firstViewAdapterPosition--;

          // we're skipping headers. they should already be vended, but if we're vending a ghostHeader
          // here an actual header will be vended if needed for measurement
          int itemViewType = adapter.getItemViewType(firstViewAdapterPosition);
          boolean isHeader = itemViewType == SectioningAdapter.TYPE_HEADER;

          // skip the header, move to next item above
          if (isHeader) {
            firstViewAdapterPosition--;
            if (firstViewAdapterPosition < 0) {
              break;
            }
          }

          View v = recycler.getViewForPosition(firstViewAdapterPosition);
          addView(v, 0);

          int bottom = getDecoratedTop(topView);
          int top;
          boolean isGhostHeader = itemViewType == SectioningAdapter.TYPE_GHOST_HEADER;
          if (isGhostHeader) {
            View header = createSectionHeaderIfNeeded(recycler,
                adapter.getSectionForAdapterPosition(firstViewAdapterPosition));
            top = bottom - getDecoratedMeasuredHeight(header); // header is already measured
          } else {
            measureChildWithMargins(v, 0, 0);
            top = bottom - getDecoratedMeasuredHeight(v);
          }

          layoutDecorated(v, left, top, right, bottom);
          topView = v;
        } else {
          break;
        }
      }
    } else {

      // content moving up, we're headed to bottom of list

      int parentHeight = getHeight();
      View bottomView = getBottommostChildView();

      while (scrolled < dy) {
        int hangingBottom = Math.max(getDecoratedBottom(bottomView) - parentHeight, 0);
        int scrollBy = -Math.min(dy - scrolled, hangingBottom);
        scrolled -= scrollBy;
        offsetChildrenVertical(scrollBy);

        int adapterPosition = getViewAdapterPosition(bottomView);
        int nextAdapterPosition = adapterPosition + 1;

        if (scrolled < dy && nextAdapterPosition < state.getItemCount()) {

          int top = getDecoratedBottom(bottomView);

          int itemViewType = adapter.getItemViewType(nextAdapterPosition);
          if (itemViewType == SectioningAdapter.TYPE_HEADER) {

            // get the header and measure it so we can followup immediately by vending the ghost header
            View headerView = createSectionHeaderIfNeeded(recycler,
                adapter.getSectionForAdapterPosition(nextAdapterPosition));
            int height = getDecoratedMeasuredHeight(headerView);
            layoutDecorated(headerView, left, 0, right, height);

            // but we need to vend the followup ghost header too
            nextAdapterPosition++;
            View ghostHeader = recycler.getViewForPosition(nextAdapterPosition);
            addView(ghostHeader);
            layoutDecorated(ghostHeader, left, top, right, top + height);
            bottomView = ghostHeader;
          } else if (itemViewType == SectioningAdapter.TYPE_GHOST_HEADER) {

            // get the header and measure it so we can followup immediately by vending the ghost header
            View headerView = createSectionHeaderIfNeeded(recycler,
                adapter.getSectionForAdapterPosition(nextAdapterPosition));
            int height = getDecoratedMeasuredHeight(headerView);
            layoutDecorated(headerView, left, 0, right, height);

            // but we need to vend the followup ghost header too
            View ghostHeader = recycler.getViewForPosition(nextAdapterPosition);
            addView(ghostHeader);
            layoutDecorated(ghostHeader, left, top, right, top + height);
            bottomView = ghostHeader;
          } else {

            View v = recycler.getViewForPosition(nextAdapterPosition);
            addView(v);

            measureChildWithMargins(v, 0, 0);
            int height = getDecoratedMeasuredHeight(v);
            layoutDecorated(v, left, top, right, top + height);
            bottomView = v;
          }
        } else {
          break;
        }
      }
    }

    View topmostView = getTopmostChildView();
    if (topmostView != null) {
      firstViewTop = getDecoratedTop(topmostView);
    }

    updateHeaderPositions(recycler);
    recycleViewsOutOfBounds(recycler);
    return scrolled;
  }

  @Override public RecyclerView.LayoutParams generateDefaultLayoutParams() {
    return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT);
  }

  @Override public boolean canScrollVertically() {
    return true;
  }

  @Override public void scrollToPosition(int position) {
    if (position < 0 || position > getItemCount()) {
      throw new IndexOutOfBoundsException("adapter position out of range");
    }

    scrollTargetAdapterPosition = position;
    pendingSavedState = null;
    requestLayout();
  }

  @Override public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state,
      int position) {
    if (position < 0 || position > getItemCount()) {
      throw new IndexOutOfBoundsException("adapter position out of range");
    }

    pendingSavedState = null;

    // see: https://blog.stylingandroid.com/scrolling-recyclerview-part-3/
    View firstVisibleChild = recyclerView.getChildAt(0);
    int itemHeight = getEstimatedItemHeightForSmoothScroll(recyclerView);
    int currentPosition = recyclerView.getChildAdapterPosition(firstVisibleChild);
    int distanceInPixels = Math.abs((currentPosition - position) * itemHeight);
    if (distanceInPixels == 0) {
      distanceInPixels = (int) Math.abs(firstVisibleChild.getY());
    }

    Context context = recyclerView.getContext();
    SmoothScroller scroller = new SmoothScroller(context, distanceInPixels);
    scroller.setTargetPosition(position);
    startSmoothScroll(scroller);
  }

  protected int getEstimatedItemHeightForSmoothScroll(RecyclerView recyclerView) {
    int height = 0;
    for (int i = 0, n = recyclerView.getChildCount(); i < n; i++) {
      height = Math.max(getDecoratedMeasuredHeight(recyclerView.getChildAt(i)), height);
    }
    return height;
  }

  protected int computeScrollVectorForPosition(int targetPosition) {
    updateFirstAdapterPosition();
    if (targetPosition > firstViewAdapterPosition) {
      return 1;
    } else if (targetPosition < firstViewAdapterPosition) {
      return -1;
    }
    return 0;
  }

  public void recycleViewsOutOfBounds(RecyclerView.Recycler recycler) {

    int height = getHeight();
    int numChildren = getChildCount();
    Set<Integer> remainingSections = new HashSet<>();
    Set<View> viewsToRecycle = new HashSet<>();

    // we do this in two passes.
    // first, recycle everything but headers
    for (int i = 0; i < numChildren; i++) {
      View view = getChildAt(i);

      if (getViewType(view) != SectioningAdapter.TYPE_HEADER) {
        if (getDecoratedBottom(view) < 0 || getDecoratedTop(view) > height) {
          viewsToRecycle.add(view);
        } else {
          // this view is visible, therefore the section lives
          remainingSections.add(getViewSectionIndex(view));
        }
      }
    }

    // second pass, for each "orphaned" header (a header who's section is completely recycled)
    // we remove it if it's gone offscreen

    for (int i = 0; i < numChildren; i++) {
      View view = getChildAt(i);
      int sectionIndex = getViewSectionIndex(view);
      if (getViewType(view) == SectioningAdapter.TYPE_HEADER && !remainingSections.contains(
          sectionIndex)) {
        float translationY = view.getTranslationY();
        if ((getDecoratedBottom(view) + translationY) < 0
            || (getDecoratedTop(view) + translationY) > height) {
          viewsToRecycle.add(view);
          headerViews.remove(view);
          headerPositionsBySection.remove(sectionIndex);
        }
      }
    }

    for (View view : viewsToRecycle) {
      removeAndRecycleView(view, recycler);
    }

    // determine the adapter adapterPosition of first visible item
    updateFirstAdapterPosition();
  }

  View getTopmostChildView() {
    if (getChildCount() == 0) {
      return null;
    }

    // note: We can't use child view order because we muck with moving things to front
    View topmostView = null;
    int top = Integer.MAX_VALUE;

    for (int i = 0, e = getChildCount(); i < e; i++) {
      View v = getChildAt(i);

      // ignore views which are being deleted
      if (getViewAdapterPosition(v) == RecyclerView.NO_POSITION) {
        continue;
      }

      // ignore headers
      if (getViewType(v) == SectioningAdapter.TYPE_HEADER) {
        continue;
      }

      int t = getDecoratedTop(v);
      if (t < top) {
        top = t;
        topmostView = v;
      }
    }

    return topmostView;
  }

  View getBottommostChildView() {
    if (getChildCount() == 0) {
      return null;
    }

    // note: We can't use child view order because we muck with moving things to front
    View bottommostView = null;
    int bottom = Integer.MIN_VALUE;

    for (int i = 0, e = getChildCount(); i < e; i++) {
      View v = getChildAt(i);

      // ignore views which are being deleted
      if (getViewAdapterPosition(v) == RecyclerView.NO_POSITION) {
        continue;
      }

      // ignore headers
      if (getViewType(v) == SectioningAdapter.TYPE_HEADER) {
        continue;
      }

      int b = getDecoratedBottom(v);
      if (b > bottom) {
        bottom = b;
        bottommostView = v;
      }
    }

    return bottommostView;
  }

  /**
   * Updates firstViewAdapterPosition to the adapter position  of the highest item in the list -
   * e.g., the
   * adapter position of the item with lowest y value in the list
   *
   * @return the y value of the topmost view in the layout, or paddingTop if empty
   */
  int updateFirstAdapterPosition() {

    // we're empty
    if (getChildCount() == 0) {
      firstViewAdapterPosition = 0;
      firstViewTop = getPaddingTop();
      return firstViewTop;
    }

    View topmostView = getTopmostChildView();
    if (topmostView != null) {
      firstViewAdapterPosition = getViewAdapterPosition(topmostView);
      firstViewTop = Math.min(topmostView.getTop(), getPaddingTop());
      return firstViewTop;
    }

    // as far as I can tell, if notifyDataSetChanged is called, onLayoutChildren
    // will be called, but all views will be marked as having NO_POSITION for
    // adapterPosition, which means the above approach of finding the topmostChildView
    // doesn't work. So, basically, leave firstViewAdapterPosition and firstViewTop
    // alone.
    return firstViewTop;
  }

  void updateHeaderPositions(RecyclerView.Recycler recycler) {

    // first, for each section represented by the current list of items,
    // ensure that the header for that section is extant

    Set<Integer> visitedSections = new HashSet<>();
    for (int i = 0, n = getChildCount(); i < n; i++) {
      View view = getChildAt(i);
      int sectionIndex = getViewSectionIndex(view);
      if (visitedSections.add(sectionIndex)) {
        createSectionHeaderIfNeeded(recycler, sectionIndex);
      }
    }

    // header is always positioned at top
    int left = getPaddingLeft();
    int right = getWidth() - getPaddingRight();

    for (View headerView : headerViews) {
      int sectionIndex = getViewSectionIndex(headerView);

      // find first and last non-header views in this section
      View ghostHeader = null;
      View firstViewInNextSection = null;
      for (int i = 0, n = getChildCount(); i < n; i++) {
        View view = getChildAt(i);
        int type = getViewType(view);
        if (type == SectioningAdapter.TYPE_HEADER) {
          continue;
        }

        int viewSectionIndex = getViewSectionIndex(view);
        if (viewSectionIndex == sectionIndex) {
          if (type == SectioningAdapter.TYPE_GHOST_HEADER) {
            ghostHeader = view;
          }
        } else if (viewSectionIndex == sectionIndex + 1) {
          if (firstViewInNextSection == null) {
            firstViewInNextSection = view;
          }
        }
      }

      int height = getDecoratedMeasuredHeight(headerView);
      int top = getPaddingTop();

      // initial position mark
      HeaderPosition headerPosition = HeaderPosition.STICKY;

      if (ghostHeader != null) {
        int ghostHeaderTop = getDecoratedTop(ghostHeader);
        if (ghostHeaderTop >= top) {
          top = ghostHeaderTop;
          headerPosition = HeaderPosition.NATURAL;
        }
      }

      if (firstViewInNextSection != null) {
        int nextViewTop = getDecoratedTop(firstViewInNextSection);
        if (nextViewTop - height < top) {
          top = nextViewTop - height;
          headerPosition = HeaderPosition.TRAILING;
        }
      }

      // now bring header to front of stack for overlap, and position it
      headerView.bringToFront();
      layoutDecorated(headerView, left, top, right, top + height);

      // notify adapter of positioning for this header
      recordHeaderPositionAndNotify(sectionIndex, headerView, headerPosition);
    }
  }

  void recordHeaderPositionAndNotify(int sectionIndex, View headerView,
      HeaderPosition newHeaderPosition) {
    if (headerPositionsBySection.containsKey(sectionIndex)) {
      HeaderPosition currentHeaderPosition = headerPositionsBySection.get(sectionIndex);
      if (currentHeaderPosition != newHeaderPosition) {
        headerPositionsBySection.put(sectionIndex, newHeaderPosition);
        if (headerPositionChangedCallback != null) {
          headerPositionChangedCallback.onHeaderPositionChanged(sectionIndex, headerView,
              currentHeaderPosition, newHeaderPosition);
        }
      }
    } else {
      headerPositionsBySection.put(sectionIndex, newHeaderPosition);
      if (headerPositionChangedCallback != null) {
        headerPositionChangedCallback.onHeaderPositionChanged(sectionIndex, headerView,
            HeaderPosition.NONE, newHeaderPosition);
      }
    }
  }

  int getViewType(View view) {
    int adapterPosition = getViewAdapterPosition(view);
    return adapter.getItemViewType(adapterPosition);
  }

  int getViewSectionIndex(View view) {
    int adapterPosition = getViewAdapterPosition(view);
    return adapter.getSectionForAdapterPosition(adapterPosition);
  }

  SectioningAdapter.ViewHolder getViewViewHolder(View view) {
    return (SectioningAdapter.ViewHolder) view.getTag(
        R.id.sectioning_adapter_tag_key_view_view_holder);
  }

  int getViewAdapterPosition(View view) {
    return getViewViewHolder(view).getAdapterPosition();
  }

  public enum HeaderPosition {
    NONE, NATURAL, STICKY, TRAILING
  }

  /**
   * Callback interface for monitoring when header positions change between members of
   * HeaderPosition enum values.
   * This can be useful if client code wants to change appearance for headers in
   * HeaderPosition.STICKY vs normal positioning.
   *
   * @see HeaderPosition
   */
  public interface HeaderPositionChangedCallback {
    /**
     * Called when a sections header positioning approach changes. The position can be
     * HeaderPosition.NONE, HeaderPosition.NATURAL, HeaderPosition.STICKY or
     * HeaderPosition.TRAILING
     *
     * @param sectionIndex the sections [0...n)
     * @param header the header view
     * @param oldPosition the previous positioning of the header (NONE, NATURAL, STICKY or
     * TRAILING)
     * @param newPosition the new positioning of the header (NATURAL, STICKY or TRAILING)
     */
    void onHeaderPositionChanged(int sectionIndex, View header, HeaderPosition oldPosition,
        HeaderPosition newPosition);
  }

  static class SavedState implements Parcelable {

    public static final Parcelable.Creator<SavedState> CREATOR =
        new Parcelable.Creator<SavedState>() {
          @Override public SavedState createFromParcel(Parcel in) {
            return new SavedState(in);
          }

          @Override public SavedState[] newArray(int size) {
            return new SavedState[size];
          }
        };
    int firstViewAdapterPosition = RecyclerView.NO_POSITION;
    int firstViewTop = 0;

    public SavedState() {
    }

    SavedState(Parcel in) {
      firstViewAdapterPosition = in.readInt();
      firstViewTop = in.readInt();
    }

    public SavedState(SavedState other) {
      firstViewAdapterPosition = other.firstViewAdapterPosition;
      firstViewTop = other.firstViewTop;
    }

    boolean isValid() {
      return firstViewAdapterPosition >= 0;
    }

    void invalidate() {
      firstViewAdapterPosition = RecyclerView.NO_POSITION;
    }

    @Override public String toString() {
      return "<"
          + this.getClass().getCanonicalName()
          + " firstViewAdapterPosition: "
          + firstViewAdapterPosition
          + " firstViewTop: "
          + firstViewTop
          + ">";
    }

    @Override public int describeContents() {
      return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
      dest.writeInt(firstViewAdapterPosition);
      dest.writeInt(firstViewTop);
    }
  }

  // https://blog.stylingandroid.com/scrolling-recyclerview-part-3/
  class SmoothScroller extends LinearSmoothScroller {
    private static final int TARGET_SEEK_SCROLL_DISTANCE_PX = 10000;
    private static final float DEFAULT_DURATION = 1000;
    private final float distanceInPixels;
    private final float duration;

    public SmoothScroller(Context context, int distanceInPixels) {
      super(context);
      this.distanceInPixels = distanceInPixels;
      float millisecondsPerPx = calculateSpeedPerPixel(context.getResources().getDisplayMetrics());
      this.duration =
          distanceInPixels < TARGET_SEEK_SCROLL_DISTANCE_PX ? (int) (Math.abs(distanceInPixels)
              * millisecondsPerPx) : DEFAULT_DURATION;
    }

    @Override public PointF computeScrollVectorForPosition(int targetPosition) {
      return new PointF(0,
          StickyHeaderLayoutManager.this.computeScrollVectorForPosition(targetPosition));
    }

    @Override protected int calculateTimeForScrolling(int dx) {
      float proportion = (float) dx / distanceInPixels;
      return (int) (duration * proportion);
    }
  }
}