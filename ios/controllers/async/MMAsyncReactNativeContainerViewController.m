//
//  MMAsyncReactNativeContainerViewController.m
//  ReactNativeAsyncLoadBundle
//
//  Created by Marcus on 2020/3/19.
//  Copyright © 2020 Facebook. All rights reserved.
//

#import "MMAsyncReactNativeContainerViewController.h"
#import "MMAsyncLoadManager.h"

@interface MMAsyncReactNativeContainerViewController ()

@end

@implementation MMAsyncReactNativeContainerViewController

- (void)viewDidLoad {
  [super viewDidLoad];
  // Do any additional setup after loading the view.
  [self.navigationItem setTitle:@"Async" ];
  RCTRootView* rootView = [[MMAsyncLoadManager getInstance] buildRootViewWithDiffBundleName:@"bundle/diff.ios"
                                                                              withExtension:@"bundle"];
  rootView.backgroundColor = [[UIColor alloc] initWithRed:1.0f green:1.0f blue:1.0f alpha:1];
  self.view = rootView;
}

- (void)viewDidDisappear:(BOOL)animated{
  [[MMAsyncLoadManager getInstance] prepareReactNativeEnv];
  [super viewDidDisappear:animated];
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
