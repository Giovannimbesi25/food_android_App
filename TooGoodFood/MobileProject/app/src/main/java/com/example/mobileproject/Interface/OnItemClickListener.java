//Simple interface which provides a method onClick to the recycleView objects.
// In this case it is used only for Menu objects, but could be used in all the recycleView.

package com.example.mobileproject.Interface;

public interface OnItemClickListener {
    void onItemClick(int position);
}
