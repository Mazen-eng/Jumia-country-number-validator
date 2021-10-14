import http from "../http-common";

class CountryNumbersDataService {
  getCustomers(params) {
    return http.get("/customers", { params });
  }

  getCountries() {
    return http.get("/countries");
  }
}

export default new CountryNumbersDataService();
