package com.ourdressingtable.common.util;

import com.ourdressingtable.common.exception.OurDressingTableException;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.mockStatic;

public class SecurityUtilMockHelper {

    public static MockedStatic<SecurityUtil> mockCurrentMemberId(Long memberId) {
        MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class);
        mockedStatic.when(SecurityUtil::getCurrentMemberId).thenReturn(memberId);
        return mockedStatic;
    }

    public static MockedStatic<SecurityUtil> mockCurrentMemberIdThrows(OurDressingTableException ex) {
        MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class);
        mockedStatic.when(SecurityUtil::getCurrentMemberId).thenThrow(ex);
        return mockedStatic;
    }
}
