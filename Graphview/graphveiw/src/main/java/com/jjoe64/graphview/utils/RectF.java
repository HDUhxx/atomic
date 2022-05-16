/*
 * Copyright (C) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jjoe64.graphview.utils;

import ohos.agp.utils.Rect;
import ohos.utils.Parcel;

import java.io.PrintWriter;

public class RectF {
    public float left;
    public float top;
    public float right;
    public float bottom;


    public RectF() {
    }


    public RectF(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }


    public RectF(RectF r) {
        if (r == null) {
            left = top = right = bottom = 0.0f;
        } else {
            left = r.left;
            top = r.top;
            right = r.right;
            bottom = r.bottom;
        }
    }

    public RectF(Rect r) {
        if (r == null) {
            left = top = right = bottom = 0.0f;
        } else {
            left = r.left;
            top = r.top;
            right = r.right;
            bottom = r.bottom;
        }
    }

    public String toString() {
        return "RectF(" + left + ", " + top + ", "
                + right + ", " + bottom + ")";
    }


    public String toShortString() {
        return toShortString(new StringBuilder(32));
    }


    public String toShortString(StringBuilder sb) {
        sb.setLength(0);
        sb.append('[');
        sb.append(left);
        sb.append(',');
        sb.append(top);
        sb.append("][");
        sb.append(right);
        sb.append(',');
        sb.append(bottom);
        sb.append(']');
        return sb.toString();
    }

    /**
     * Print short representation to given writer.
     *
     * @param pw str
     */
    public void printShortString(PrintWriter pw) {
        pw.print('[');
        pw.print(left);
        pw.print(',');
        pw.print(top);
        pw.print("][");
        pw.print(right);
        pw.print(',');
        pw.print(bottom);
        pw.print(']');
    }

    /**
     * Returns true if the rectangle is empty (left >= right or top >= bottom)
     *
     * @return str
     */
    public final boolean isEmpty() {
        return left >= right || top >= bottom;
    }

    /**
     * str
     *
     * @return the rectangle's width. This does not check for a valid rectangle
     * (i.e. left <= right) so the result may be negative.
     */
    public final float width() {
        return right - left;
    }

    /**
     * str
     *
     * @return the rectangle's height. This does not check for a valid rectangle
     * (i.e. top <= bottom) so the result may be negative.
     */
    public final float height() {
        return bottom - top;
    }

    /**
     * str
     *
     * @return the horizontal center of the rectangle. This does not check for
     * a valid rectangle (i.e. left <= right)
     */
    public final float centerX() {
        return (left + right) * 0.5f;
    }

    /**
     * str
     *
     * @return the vertical center of the rectangle. This does not check for
     * a valid rectangle (i.e. top <= bottom)
     */
    public final float centerY() {
        return (top + bottom) * 0.5f;
    }

    /**
     * Set the rectangle to (0,0,0,0)
     */
    public void setEmpty() {
        left = right = top = bottom = 0;
    }

    /**
     * Set the rectangle's coordinates to the specified values. Note: no range
     * checking is performed, so it is up to the caller to ensure that
     * left <= right and top <= bottom.
     *
     * @param left   The X coordinate of the left side of the rectangle
     * @param top    The Y coordinate of the top of the rectangle
     * @param right  The X coordinate of the right side of the rectangle
     * @param bottom The Y coordinate of the bottom of the rectangle
     */
    public void set(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    /**
     * Copy the coordinates from src into this rectangle.
     *
     * @param src The rectangle whose coordinates are copied into this
     *            rectangle.
     */
    public void set(RectF src) {
        this.left = src.left;
        this.top = src.top;
        this.right = src.right;
        this.bottom = src.bottom;
    }

    /**
     * Copy the coordinates from src into this rectangle.
     *
     * @param src The rectangle whose coordinates are copied into this
     *            rectangle.
     */
    public void set(Rect src) {
        this.left = src.left;
        this.top = src.top;
        this.right = src.right;
        this.bottom = src.bottom;
    }

    /**
     * Offset the rectangle by adding dx to its left and right coordinates, and
     * adding dy to its top and bottom coordinates.
     *
     * @param dx The amount to add to the rectangle's left and right coordinates
     * @param dy The amount to add to the rectangle's top and bottom coordinates
     */
    public void offset(float dx, float dy) {
        left += dx;
        top += dy;
        right += dx;
        bottom += dy;
    }

    /**
     * Offset the rectangle to a specific (left, top) position,
     * keeping its width and height the same.
     *
     * @param newLeft The new "left" coordinate for the rectangle
     * @param newTop  The new "top" coordinate for the rectangle
     */
    public void offsetTo(float newLeft, float newTop) {
        right += newLeft - left;
        bottom += newTop - top;
        left = newLeft;
        top = newTop;
    }

    /**
     * str
     *
     * @param dx str
     * @param dy str
     */
    public void inset(float dx, float dy) {
        left += dx;
        top += dy;
        right -= dx;
        bottom -= dy;
    }

    /**
     * str
     *
     * @param x str
     * @param y str
     * @return str
     */
    public boolean contains(float x, float y) {
        return left < right && top < bottom  // check for empty first
                && x >= left && x < right && y >= top && y < bottom;
    }

    /**
     * str
     *
     * @param left str
     * @param top str
     * @param right str
     * @param bottom str
     * @return str
     */
    public boolean contains(float left, float top, float right, float bottom) {
        // check for empty first
        return this.left < this.right && this.top < this.bottom
                // now check for containment
                && this.left <= left && this.top <= top
                && this.right >= right && this.bottom >= bottom;
    }

    /**
     * str
     *
     * @param r str
     * @return str
     */
    public boolean contains(RectF r) {
        // check for empty first
        return this.left < this.right && this.top < this.bottom
                // now check for containment
                && left <= r.left && top <= r.top
                && right >= r.right && bottom >= r.bottom;
    }

    /**
     * str
     *
     * @param left dtr
     * @param top ss
     * @param right ss
     * @param bottom ss
     * @return ss
     */
    public boolean intersect(float left, float top, float right, float bottom) {
        if (this.left < right && left < this.right
                && this.top < bottom && top < this.bottom) {
            if (this.left < left) {
                this.left = left;
            }
            if (this.top < top) {
                this.top = top;
            }
            if (this.right > right) {
                this.right = right;
            }
            if (this.bottom > bottom) {
                this.bottom = bottom;
            }
            return true;
        }
        return false;
    }

    /**
     * ss
     *
     * @param r ss
     * @return ss
     */
    public boolean intersect(RectF r) {
        return intersect(r.left, r.top, r.right, r.bottom);
    }

    /**
     * ss
     *
     * @param a ss
     * @param b ss
     * @return ss
     */
    public boolean setIntersect(RectF a, RectF b) {
        if (a.left < b.right && b.left < a.right
                && a.top < b.bottom && b.top < a.bottom) {
            left = Math.max(a.left, b.left);
            top = Math.max(a.top, b.top);
            right = Math.min(a.right, b.right);
            bottom = Math.min(a.bottom, b.bottom);
            return true;
        }
        return false;
    }

    /**
     * ss
     *
     * @param left ss
     * @param top ss
     * @param right ss
     * @param bottom ss
     * @return ss
     */
    public boolean intersects(float left, float top, float right,
                              float bottom) {
        return this.left < right && left < this.right
                && this.top < bottom && top < this.bottom;
    }

    /**
     * ss
     *
     * @param a ss
     * @param b ss
     * @return ss
     */
    public static boolean intersects(RectF a, RectF b) {
        return a.left < b.right && b.left < a.right
                && a.top < b.bottom && b.top < a.bottom;
    }

    /**
     * Set the dst integer Rect by rounding this rectangle's coordinates
     * to their nearest integer values.
     *
     * @param dst str
     */
    public void round(Rect dst) {
        dst.set((int) left, (int) top,
                (int) right, (int) bottom);
    }

    /**
     * ss
     *
     * @param dst ss
     */
    public void roundOut(Rect dst) {
        dst.set((int) Math.floor(left), (int) Math.floor(top),
                (int) Math.ceil(right), (int) Math.ceil(bottom));
    }

    /**
     * ss
     *
     * @param left ss
     * @param top ss
     * @param right ss
     * @param bottom ss
     */
    public void union(float left, float top, float right, float bottom) {
        if ((left < right) && (top < bottom)) {
            if ((this.left < this.right) && (this.top < this.bottom)) {
                if (this.left > left)
                    this.left = left;
                if (this.top > top)
                    this.top = top;
                if (this.right < right)
                    this.right = right;
                if (this.bottom < bottom)
                    this.bottom = bottom;
            } else {
                this.left = left;
                this.top = top;
                this.right = right;
                this.bottom = bottom;
            }
        }
    }

    /**
     * ss
     *
     * @param r ss
     */
    public void union(RectF r) {
        union(r.left, r.top, r.right, r.bottom);
    }

    /**
     * ss
     *
     * @param x ss
     * @param y ss
     */
    public void union(float x, float y) {
        if (x < left) {
            left = x;
        } else if (x > right) {
            right = x;
        }
        if (y < top) {
            top = y;
        } else if (y > bottom) {
            bottom = y;
        }
    }

    /**
     * ss
     */
    public void sort() {
        if (left > right) {
            float temp = left;
            left = right;
            right = temp;
        }
        if (top > bottom) {
            float temp = top;
            top = bottom;
            bottom = temp;
        }
    }

    /**
     * ss
     *
     * @return ss
     */
    public int describeContents() {
        return 0;
    }

    /**
     * ss
     *
     * @param out ss
     * @param flags ss
     */
    public void writeToParcel(Parcel out, int flags) {
        out.writeFloat(left);
        out.writeFloat(top);
        out.writeFloat(right);
        out.writeFloat(bottom);
    }

    /**
     * ss
     *
     * @param scale ss
     */
    public void scale(float scale) {
        if (scale != 1.0f) {
            left = left * scale;
            top = top * scale;
            right = right * scale;
            bottom = bottom * scale;
        }
    }
}
