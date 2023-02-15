package com.kgltrash.controller;
import com.kgltrash.callback.GetReceipts;

import java.io.Serializable;

/**
 * Author by Yves Byiringiro
 */
public interface CreateAnalytics extends Serializable {
    public void getPaymentReceipts(GetReceipts getReceiptsCallBack);

}
