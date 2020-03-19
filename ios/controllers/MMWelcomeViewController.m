//
//  MMWelcomeViewController.m
//  ReactNativeAsyncLoadBundle
//
//  Created by Marcus on 2020/3/16.
//  Copyright © 2020 Facebook. All rights reserved.
//

#import "MMWelcomeViewController.h"
#import "MMSyncReactNativeContainerViewController.h"
#import "MMAsyncGuideViewController.h"
#import "Masonry.h"
#import "MMTimeRecordUtil.h"

@interface MMWelcomeViewController ()

@end

@implementation MMWelcomeViewController

- (void)viewDidLoad {
  [super viewDidLoad];
  // Do any additional setup after loading the view.
  [self.navigationItem setTitle:@"Welcome"];
  
  CGRect screenRect = [[UIScreen mainScreen] bounds];
  
  UILabel *labelTips = [[UILabel alloc] init];
  labelTips.text = @"There are two ways to start an activity with react naitve in the demo: one as SYNC, the other as ASYNC. It is same with the offical reference implementation when using SYNC. As for ASYNC, it will start a general activity, which will load a common bundle file, after that it will start a custom activity, which will only load the differential bundle file. The load time of react view will display by log and toast.";
  labelTips.font = [UIFont systemFontOfSize:18];
  labelTips.lineBreakMode = NSLineBreakByWordWrapping;
  labelTips.numberOfLines = 0;
  [self.view addSubview:labelTips];
  
  [labelTips mas_makeConstraints:^(MASConstraintMaker *make) {
    make.height.equalTo(@240);
    make.left.equalTo(self.view).offset(20);
    make.right.equalTo(self.view).offset(-20);
    make.centerY.equalTo(self.view.mas_top).offset(screenRect.size.height/5 + 20);
    make.centerX.mas_equalTo(self.view);
  }];
  
  UILabel *labelNotice = [[UILabel alloc] init];
  labelNotice.text = @"NOTICE: If you want to get the load time accurately, you should restart the app before clicking one of the bottom two buttons.";
  labelNotice.textColor = UIColor.redColor;
  labelNotice.lineBreakMode = NSLineBreakByWordWrapping;
  labelNotice.numberOfLines = 0;
  [self.view addSubview:labelNotice];
  
  [labelNotice mas_makeConstraints:^(MASConstraintMaker *make) {
    make.height.equalTo(@100);
    make.left.equalTo(self.view).offset(20);
    make.right.equalTo(self.view).offset(-20);
    make.centerY.equalTo(self.view.mas_top).offset(screenRect.size.height/5*2);
    make.centerX.equalTo(self.view);
  }];
  
  UIButton *btnSync = [[UIButton alloc] init];
  [btnSync setTitle:@"Sync Load RN Bundle" forState:UIControlStateNormal];
  [btnSync setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
  [btnSync setBackgroundColor:[UIColor lightGrayColor]];
  [btnSync setTag: 1];
  [btnSync addTarget:self
              action:@selector(btnClickListener:)
    forControlEvents:UIControlEventTouchUpInside];
  [self.view addSubview:btnSync];
  
  [btnSync mas_makeConstraints:^(MASConstraintMaker *make) {
    make.left.equalTo(self.view).offset(20);
    make.right.equalTo(self.view).offset(-20);
    make.height.equalTo(@60);
    make.centerY.equalTo(self.view.mas_top).offset(screenRect.size.height/5*3);
    make.centerX.mas_equalTo(self.view);
  }];
  
  UIButton *btnAsync = [[UIButton alloc] init];
  [btnAsync setTitle:@"Async Load RN Bundle" forState:UIControlStateNormal];
  [btnAsync setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
  [btnAsync setBackgroundColor:[UIColor lightGrayColor]];
  [btnAsync setTag: 2];
  [btnAsync addTarget:self
              action:@selector(btnClickListener:)
    forControlEvents:UIControlEventTouchUpInside];
  [self.view addSubview:btnAsync];
  
  [btnAsync mas_makeConstraints:^(MASConstraintMaker *make) {
    make.left.equalTo(self.view).offset(20);
    make.right.equalTo(self.view).offset(-20);
    make.height.equalTo(@60);
    make.centerY.equalTo(self.view.mas_top).offset(screenRect.size.height/5*4);
    make.centerX.equalTo(self.view);
  }];
  
}

-(void)btnClickListener:(UIButton *) button{
  if(button.tag == 1){
    // sync Button
    NSLog(@"Sync Button Clicked.");
    MMSyncReactNativeContainerViewController *controller = [[MMSyncReactNativeContainerViewController alloc] init];
    [self.navigationController pushViewController:controller animated:NO];
    [[MMTimeRecordUtil getInstance] setStartTime:@"ViewAction"];
  }else if(button.tag == 2){
    // async Button
    NSLog(@"Async Button Clicked.");
    MMAsyncGuideViewController *controller = [[MMAsyncGuideViewController alloc] init];
    [self.navigationController pushViewController:controller animated:NO];
  }else {
    
  }
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
