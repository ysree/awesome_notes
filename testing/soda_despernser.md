
# Soda Dispensing Machine Scenario

#### 1. Objective
Simulate and test a **soda vending machine** that dispenses beverages based on user selection and payment.  
Ensure correct behavior for various inputs, payments, and stock conditions.

---

#### 2. Features
1. **User Interaction**
   - Display available soda types and prices.
   - Accept user input for soda selection.

2. **Payment Handling**
   - Accept coins or digital payments.
   - Validate payment amount.
   - Return change if payment exceeds cost.

3. **Inventory Management**
   - Track stock of each soda type.
   - Notify when stock is low or out of stock.
   - Prevent dispensing if stock is insufficient.

4. **Dispensing**
   - Dispense the selected soda if payment is sufficient and stock is available.
   - Display confirmation after successful dispensing.

5. **Error Handling**
   - Insufficient payment → display error.
   - Out of stock → display error.
   - Invalid selection → display error.

---

#### 3. Functional Flow
1. User selects a soda from available options.  
2. Machine prompts for payment.  
3. User inserts coins or pays digitally.  
4. Machine checks:
   - Is the payment sufficient?  
   - Is the selected soda in stock?  
5. If yes → dispense soda and return change if needed.  
6. If no → display appropriate error message.

---

#### 4. Test Cases

| Test Case ID | Scenario | Input | Expected Result |
|--------------|---------|-------|----------------|
| TC1 | Valid selection & exact payment | Select Cola, insert $1 | Dispense Cola, no change |
| TC2 | Valid selection & excess payment | Select Sprite, insert $2 | Dispense Sprite, return $1 change |
| TC3 | Valid selection & insufficient payment | Select Fanta, insert $0.5 | Error: Insufficient payment |
| TC4 | Out of stock selection | Select Pepsi (stock=0) | Error: Out of stock |
| TC5 | Invalid selection | Select Lemonade (not available) | Error: Invalid selection |
| TC6 | Multiple purchases sequentially | Select Cola, pay, then select Fanta | Each purchase handled independently |
| TC7 | Low stock warning | Stock=1 for Sprite, select Sprite | Dispense Sprite, show low stock warning |
| TC8 | Cancel transaction before payment | Select Coke, cancel | Transaction canceled, no payment deducted |
| TC9 | Digital payment | Select Pepsi, pay via card | Dispense Pepsi, verify digital payment processed |
| TC10 | Machine resets after failure | Simulate power outage | Machine restores inventory and functionality after restart |

---

#### 5. Edge Cases
- Multiple users trying to purchase at the same time.  
- Invalid coin insertion (fake or wrong denomination).  
- Network failure during digital payment.  
- Machine running out of change for excess payment.  

---

#### 6. Non-Functional Considerations
- **Performance:** Dispense soda within 5 seconds after payment.  
- **Reliability:** System should handle 1000 transactions/day without failure.  
- **Usability:** Clear user interface and error messages.  
- **Security:** Prevent tampering with payment and inventory systems.  

---

#### Summary
The **Soda Dispensing Machine scenario** covers **functional and non-functional requirements, user interactions, payment handling, inventory management, error handling, and edge cases**.  
Testing ensures **accuracy, usability, and reliability** of the vending machine system.