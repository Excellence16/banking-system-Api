
package com.excellence.bankingsystemapi.exceptions.global;

import com.excellence.bankingsystemapi.exceptions.account.BusinessException;

/**
 * Thrown when communication with
 * an external service fails.
 *
 * Examples:
 * - BVN Verification
 * - Payment Gateway
 * - SMS Provider
 * - Email Provider
 */
public class ExternalServiceException extends BusinessException {

    public ExternalServiceException(String message) {
        super(message, ErrorCode.EXTERNAL_SERVICE_ERROR);
    }
}