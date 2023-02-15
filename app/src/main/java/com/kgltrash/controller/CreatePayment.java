package com.kgltrash.controller;

import com.kgltrash.callback.HandleAsync;
import com.kgltrash.model.Payment;

import java.io.Serializable;

/**
 * Author by Yves Byiringiro
 */
public interface CreatePayment extends Serializable {
    public void submitPaymentReceipt(Payment payment, HandleAsync onPayment);

}
