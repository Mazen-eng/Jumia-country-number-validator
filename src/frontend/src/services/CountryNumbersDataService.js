import http from "../http-common";

class CountryNumbersDataService {
  getCustomers(params) {
    return http.get("/customers/find", { params });
  }

  getCountries(){
    return http.get("/countries/get");
  }

}

export default new CountryNumbersDataService();