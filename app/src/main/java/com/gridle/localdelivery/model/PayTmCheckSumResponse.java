package com.gridle.localdelivery.model;

public class PayTmCheckSumResponse {
    private Result result;
    private String paytmResponse;

    public PayTmCheckSumResponse(Result result, String paytmResponse) {
        this.result = result;
        this.paytmResponse = paytmResponse;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getPaytmResponse() {
        return paytmResponse;
    }

    public void setPaytmResponse(String paytmResponse) {
        this.paytmResponse = paytmResponse;
    }

    public static class Result {
        private String signature;
        private boolean verified;

        public Result(String signature, boolean verified) {
            this.signature = signature;
            this.verified = verified;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public boolean getVerified() {
            return verified;
        }

        public void setVerified(boolean verified) {
            this.verified = verified;
        }
    }
}
