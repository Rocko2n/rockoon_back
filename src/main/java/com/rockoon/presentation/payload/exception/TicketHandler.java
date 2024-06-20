package com.rockoon.presentation.payload.exception;

import com.rockoon.presentation.payload.code.BaseCode;

public class TicketHandler extends GeneralException{
    public TicketHandler(BaseCode code) {
        super(code);
    }
}
