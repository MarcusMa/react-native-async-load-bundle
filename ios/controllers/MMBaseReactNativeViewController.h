//
//  BaseReactNativeViewController.h
//  ReactNativeAsyncLoadBundle
//
//  Created by Marcus on 2020/3/19.
//  Copyright © 2020 Facebook. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <React/RCTBridge.h>
#import <React/RCTBundleURLProvider.h>
#import <React/RCTRootView.h>
#import "CRToast.h"

NS_ASSUME_NONNULL_BEGIN

@interface MMBaseReactNativeViewController : UIViewController

- (void)showToast:(NSString*) msg;

@end

NS_ASSUME_NONNULL_END
