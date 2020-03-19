//
//  BaseReactNativeViewController.m
//  ReactNativeAsyncLoadBundle
//
//  Created by Marcus on 2020/3/19.
//  Copyright © 2020 Facebook. All rights reserved.
//

#import "MMBaseReactNativeViewController.h"
#import "MMTimeRecordUtil.h"

@interface MMBaseReactNativeViewController ()

@end

@implementation MMBaseReactNativeViewController

- (void)viewDidLoad {
  [super viewDidLoad];
  [[MMTimeRecordUtil getInstance] setStartTime:@"RNLoad"];
  
  // Do any additional setup after loading the view.
  [[NSNotificationCenter defaultCenter] addObserver:self
                                           selector:@selector(handleBridgeDidLoadJavaScriptNotification:)
                                               name:RCTContentDidAppearNotification
                                             object:nil];
}

- (void)handleBridgeDidLoadJavaScriptNotification:(NSNotification *)notification
{
  [[MMTimeRecordUtil getInstance] setEndTime:@"RNLoad"];
  [[MMTimeRecordUtil getInstance] setEndTime:@"ViewAction"];
  [[MMTimeRecordUtil getInstance] printTimeInfo:@"RNLoad"];
  [[MMTimeRecordUtil getInstance] printTimeInfo:@"ViewAction"];
  
  NSString* msg = [[NSString alloc] initWithFormat:@"Action Time Cost: %@ \r\n RN Time Cost: %@",
                   [[MMTimeRecordUtil getInstance] getReadableTimeCostWithUnit: @"ViewAction"],
                    [[MMTimeRecordUtil getInstance] getReadableTimeCostWithUnit: @"RNLoad"]];
  
  [self showToast:msg];
}

- (void) showToast:(NSString*) msg{
  NSDictionary *options = @{kCRToastTextKey : msg,
                            kCRToastNotificationTypeKey:@(CRToastTypeNavigationBar),
                            kCRToastTextAlignmentKey : @(NSTextAlignmentCenter),
                            kCRToastBackgroundColorKey : [UIColor grayColor],
                            kCRToastAnimationInTypeKey : @(CRToastAnimationTypeLinear),
                            kCRToastAnimationOutTypeKey : @(CRToastAnimationTypeLinear),
                            kCRToastAnimationInDirectionKey : @(CRToastAnimationDirectionTop),
                            kCRToastAnimationOutDirectionKey : @(CRToastAnimationDirectionTop)
  };
  
  
  [CRToastManager showNotificationWithOptions:options
                              completionBlock:^{
    NSLog(@"Completed");
  }];
}

- (void)dealloc
{
  NSLog(@"dealloc");
  [[NSNotificationCenter defaultCenter] removeObserver:self name:RCTContentDidAppearNotification object:nil];
}

/*
 #pragma mark - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */

@end
