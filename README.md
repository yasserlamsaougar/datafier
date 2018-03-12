# datafier
[I only spent 2h on the project so there is a lot of things to do and correct] 

This project allows to create test data and thrives to allow the user to connect his data for example.
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
This configuration describes a list of simple objects and groups. 
A simple object has a name and a list of attributes which are formats used to generate it randomly using a certain generator.
So far three generator types exist [faker,regex,enum] 

`"building": "faker.building"` will generate a random building number
`"row_id": "regex.id_cloe"` will generate a random id based on regex located in the regex configuration file
`"x_niveau": "enum.account_level"` will generate a random level based on a list of value located in the enum configuration file

A group is a collection of simple objects and in the future other groups that can be connected together via 
the overrides property. In this example we're saying that the row_id property of address should be the same as par_addr_id of the account.
The overrides property doesn't handle n to n relations at best it can handles 1 to n relations so there is still room for improvement.
