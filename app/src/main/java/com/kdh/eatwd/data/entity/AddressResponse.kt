package com.kdh.eatwd.data.entity

data class AddressResponse(
    val results: Results
) {
    data class Results(
        val common: Common,
        val juso: List<Juso>
    ) {
        data class Common(
            val countPerPage: String,
            val currentPage: String,
            val errorCode: String,
            val errorMessage: String,
            val totalCount: String
        )

        data class Juso(
            val admCd: String,
            val bdKdcd: String,
            val bdMgtSn: String,
            val bdNm: String,
            val buldMnnm: String,
            val buldSlno: String,
            val detBdNmList: String,
            val emdNm: String,
            val emdNo: String,
            val engAddr: String,
            val jibunAddr: String,
            val liNm: String,
            val lnbrMnnm: String,
            val lnbrSlno: String,
            val mtYn: String,
            val rn: String,
            val rnMgtSn: String,
            val roadAddr: String,
            val roadAddrPart1: String,
            val roadAddrPart2: String,
            val sggNm: String,
            val siNm: String,
            val udrtYn: String,
            val zipNo: String
        )
    }
}