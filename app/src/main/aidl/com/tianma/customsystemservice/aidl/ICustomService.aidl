// ICustomService.aidl
package com.tianma.customsystemservice.aidl;

// Declare any non-default types here with import statements
/* {@hide} */
interface ICustomService {
    /* toUppercase */
    String toUpperCase(String str);
    /* add numbers */
    int add(int num1, int num2);
}
