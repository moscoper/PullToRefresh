package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by cuifei on 16/7/13.
 */
public class PullRefreshRecyclerView extends PullToRefreshBase<RecyclerView> {
  public PullRefreshRecyclerView(Context context, Mode mode) {
    super(context, mode);
  }

  public PullRefreshRecyclerView(Context context) {
    super(context);
  }

  public PullRefreshRecyclerView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public PullRefreshRecyclerView(Context context, Mode mode, AnimationStyle animStyle) {
    super(context, mode, animStyle);
  }

  @Override public Orientation getPullToRefreshScrollDirection() {
    return Orientation.HORIZONTAL;
  }

  @Override protected RecyclerView createRefreshableView(Context context, AttributeSet attrs) {
    RecyclerView recyclerView = new RecyclerView(context,attrs);
    LinearLayoutManager mannagerTwo = new LinearLayoutManager(context);
    mannagerTwo.setOrientation(LinearLayoutManager.HORIZONTAL);
    //recyclerView.addItemDecoration(
    //    new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL_LIST));
    recyclerView.setLayoutManager(mannagerTwo);

    return recyclerView;
  }

  public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration){
    mRefreshableView.addItemDecoration(itemDecoration);
  }

  public void setAdapter(RecyclerView.Adapter adapter){
    mRefreshableView.setAdapter(adapter);
  }

  @Override protected boolean isReadyForPullEnd() {
    return isLastItemVisible();
  }

  @Override protected boolean isReadyForPullStart() {
    return isFirstItemVisible();
  }

  private boolean isLastItemVisible() {
    final RecyclerView.Adapter adapter = mRefreshableView.getAdapter();

    if (null == adapter || adapter.getItemCount()==0) {
      if (DEBUG) {
        Log.d(LOG_TAG, "isLastItemVisible. Empty View.");
      }
      return true;
    } else {
      final int lastItemPosition = adapter.getItemCount() - 1;
      final int lastVisiblePosition = getLastVisiblePosition();

      if (DEBUG) {
        Log.d(LOG_TAG, "isLastItemVisible. Last Item Position: "
            + lastItemPosition
            + " Last Visible Pos: "
            + lastVisiblePosition);
      }

      /**
       * This check should really just be: lastVisiblePosition ==
       * lastItemPosition, but PtRListView internally uses a FooterView
       * which messes the positions up. For me we'll just subtract one to
       * account for it and rely on the inner condition which checks
       * getBottom().
       */
      if (lastVisiblePosition >= lastItemPosition - 1) {
        final int childIndex = lastVisiblePosition - getFirstVisiblePosition();
        final View lastVisibleChild = mRefreshableView.getChildAt(childIndex);
        if (lastVisibleChild != null) {
          return lastVisibleChild.getRight() <= mRefreshableView.getRight();
        }
      }
    }

    return false;
  }

  private int getFirstVisiblePosition(){
    int position = 0;
   RecyclerView.LayoutManager manager =  mRefreshableView.getLayoutManager();
    if (manager instanceof LinearLayoutManager){
      LinearLayoutManager linearLayoutManager = (LinearLayoutManager) manager;
      return  linearLayoutManager.findFirstVisibleItemPosition();
    }

    return position;
  }

  private int getLastVisiblePosition(){
    int position = 0;
    RecyclerView.LayoutManager manager =  mRefreshableView.getLayoutManager();
    if (manager instanceof LinearLayoutManager){
      LinearLayoutManager linearLayoutManager = (LinearLayoutManager) manager;
      return  linearLayoutManager.findLastVisibleItemPosition();
    }
    return position;
  }

  private boolean isFirstItemVisible() {
    final RecyclerView.Adapter adapter = mRefreshableView.getAdapter();

    if (null == adapter || adapter.getItemCount() ==0) {
      if (DEBUG) {
        Log.d(LOG_TAG, "isFirstItemVisible. Empty View.");
      }
      return true;
    } else {

      /**
       * This check should really just be:
       * mRefreshableView.getFirstVisiblePosition() == 0, but PtRListView
       * internally use a HeaderView which messes the positions up. For
       * now we'll just add one to account for it and rely on the inner
       * condition which checks getTop().
       */
      if (getFirstVisiblePosition() <= 1) {
        final View firstVisibleChild = mRefreshableView.getChildAt(0);
        if (firstVisibleChild != null) {
          return firstVisibleChild.getLeft() >= mRefreshableView.getLeft();
        }
      }
    }

    return false;
  }

}
