//
//  RCTBridge.h
//  ReactNativeAsyncLoadBundle
//
//  Created by Marcus on 2020/3/19.
//  Copyright © 2020 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
 
@interface RCTBridge (RnLoadJS)
 
 - (void)executeSourceCode:(NSData *)sourceCode sync:(BOOL)sync;
 
@end
