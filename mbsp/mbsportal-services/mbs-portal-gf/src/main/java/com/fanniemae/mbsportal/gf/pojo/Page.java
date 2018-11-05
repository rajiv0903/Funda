/*
 *
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 *
 */

package com.fanniemae.mbsportal.gf.pojo;

import java.io.Serializable;

/**
 * @author g8uaxt Created on 12/14/2017.
 */

@SuppressWarnings("serial")
public class Page implements Serializable {
        int size;
        int position;
        
      /*  public Page() {
        }*/
        
        public Page(int size, int position) {
                this.size = size;
                this.position = position;
        }
        
        public int getSize() {
                return size;
        }
        
        public void setSize(int size) {
                this.size = size;
        }
        
        public int getPosition() {
                return position;
        }
        
        public void setPosition(int position) {
                this.position = position;
        }
        
        @Override
        public String toString() {
                return "Page{" + "size=" + size + ", position=" + position + '}';
        }
}
