# datafier
[I only spent 2h on the project so there is a lot of things to do and correct] 

This project allows to create test data and thrives to allow the user to connect his data for example : 
 ```
 "definitions": {
    "address": {
      "attributes": {
        "row_id": "regex.id_cloe",
        "city": "faker.city",
        "building": "faker.building",
        "postal_code": "faker.zip_code",
        "address": "faker.street"
      }
    },
    "account": {
      "attributes": {
        "row_id": "regex.id_cloe",
        "par_addr_id": "regex.id_cloe",
        "x_niveau": "enum.account_level"
      }
    }
  },
  "groups": {
    "account": {
      "objects": {
        "address": {
          "count": 1
        },
        "account": {
          "count": 1
        }
      },
      "overrides": {
        "address.row_id": "account.par_addr_id"
      }
    }
  }
}
```
This configuration describes a list of simple objects and groups. A group is a collection of simple objects that can be connected together via 
the overrides property. In this example we're saying that the row_id property of address is the same as par_addr_id of the account.
