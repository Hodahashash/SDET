Feature: USD Exchange Rates API

  Scenario: Successful API call and valid price
    Given the USD exchange API is available
    When I make a request to fetch USD exchange rates
    Then the API call is successful and returns valid price

  Scenario: Unexpected API status
    Given the USD exchange API is available
    When I make a request to fetch USD exchange rates
    Then the API call returns an unexpected status