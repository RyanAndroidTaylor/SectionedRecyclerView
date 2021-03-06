/*
 * Copyright (C) 2015 thewaronryan@gmail.com
 * Added code to handle sections
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.deserttowersprogramming.sectioned;

import android.support.v7.widget.RecyclerView;

public interface SectionAdapter<VH extends RecyclerView.ViewHolder, T> {
    boolean isNormalView(int position);
    boolean isFooter(int position);

    void setViewTypes();

    /**
     * @param item The item the section will come before
     * @return True if a section should be create before this item in the RecyclerView
     */
    boolean needsSectionBefore(T item);

    int getAdjustedPositionForSections(int position);

    void enableFooter();
}
