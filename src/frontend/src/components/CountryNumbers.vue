<template>
  <v-row align="center" class="list px-3 mx-auto">
    <v-col cols="12" sm="12">
      <v-row class="ma-3">
        <v-col cols="4" sm="3">
          <v-select
            v-model="countryCategory"
            :items="countryCategories"
            label="Filter by country"
            @change="handleCountryCategoryChange"
            color = "orange darken-1"
            item-color = "orange darken-1"
          ></v-select>
        </v-col>
        <v-col cols="4" sm="3">
          <v-select
            v-model="state"
            :items="states"
            label="Filter by state"
            @change="handleStateChange"
            color = "orange darken-1"
            item-color = "orange darken-1"
          ></v-select>
        </v-col>
      </v-row>
    </v-col>

    <v-col cols="12" sm="12">
      <v-card class="mx-auto" tile>
        <v-card-title>Validated Numbers</v-card-title>
        <v-data-table
          :headers="headers"
          :items="numbers"
          disable-pagination
          :hide-default-footer="true"
        >
          <template v-slot:item.valid="{ item }">
            <v-icon small class="mr-2" v-if="item.valid" color="green"
              >mdi-check-circle</v-icon
            >
            <v-icon small v-if="!item.valid" color="red"
              >mdi-close-circle</v-icon
            >
          </template>
        </v-data-table>
      </v-card>
    </v-col>
    <v-col cols="12" sm="12">
      <v-row class="ma-3">
        <v-col cols="4" sm="3">
          <v-select
            v-model="pageSize"
            :items="pageSizes"
            label="Items per Page"
            @change="handlePageSizeChange"
            color = "orange darken-1"
            item-color = "orange darken-1"
          ></v-select>
        </v-col>

        <v-col cols="12" sm="9">
          <v-pagination
            v-model="page"
            :length="totalPages"
            total-visible="7"
            next-icon="mdi-menu-right"
            prev-icon="mdi-menu-left"
            @input="handlePageChange"
            color = "orange darken-1"
          ></v-pagination>
        </v-col>
      </v-row>
    </v-col>
  </v-row>
</template>

<script>
import CountryNumbersDataService from "../services/CountryNumbersDataService";
export default {
  name: "validated-numbers-list",
  data() {
    return {
      numbers: [],
      headers: [
        {
          text: "Customer name",
          align: "start",
          sortable: true,
          value: "customerName",
        },
        { text: "Country code", value: "countryCode", sortable: true },
        { text: "Phone number", value: "phoneNumber", sortable: true },
        { text: "Country name", value: "countryName", sortable: true },
        { text: "State", value: "valid", sortable: true },
      ],
      page: 1,
      totalPages: 0,
      pageSize: 5,
      pageSizes: [5, 10, 15],
      countryCategory: "All",
      countryCategories: ["All"],
      state: "All",
      states: [
        "All",
        "Valid",
        "Invalid"
      ],
    };
  },
  methods: {
    getRequestParams(page, pageSize, countryCategory, state) {
      let params = {};

      if (page) {
        params["page"] = page - 1;
      }

      if (pageSize) {
        params["size"] = pageSize;
      }

      if (countryCategory) {
        params["countryCategory"] = countryCategory;
      }

      if (state) {
        params["state"] = state;
      }

      return params;
    },
    retrieveValidatedNumbers() {
      const params = this.getRequestParams(
        this.page,
        this.pageSize,
        this.countryCategory,
        this.state
      );

      CountryNumbersDataService.getCustomers(params)
        .then((response) => {
          const { totalPages, numbers } = response.data;
          this.numbers = numbers;
          this.numbers = this.numbers.map(this.getDisplayNumbers);
          this.totalPages = totalPages;
        })
        .catch((e) => {
          console.log(e);
        });
    },

    handlePageChange() {
      this.retrieveValidatedNumbers();
    },

    handlePageSizeChange() {
      this.page = 1;
      this.retrieveValidatedNumbers();
    },

    handleCountryCategoryChange() {
      this.page = 1;
      this.retrieveValidatedNumbers();
    },

    handleStateChange() {
      this.page = 1;
      this.retrieveValidatedNumbers();
    },

    loadCountries() {
      CountryNumbersDataService.getCountries()
        .then((response) => {
          this.countryCategories.push.apply(this.countryCategories, response.data);
        })
        .catch((e) => {
          console.log(e);
        });
      
    },

    getDisplayNumbers(number) {
      return {
        id: number.id,
        customerName: number.customerName,
        countryCode: number.countryCode,
        countryName: number.countryName,
        phoneNumber: number.phoneNumber,
        valid: number.valid,
      };
    },
  },
  mounted() {
    this.loadCountries();
    this.retrieveValidatedNumbers();
  },
};
</script>

<style>
.list {
  max-width: 750px;
}
</style>
