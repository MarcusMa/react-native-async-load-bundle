//
//  MMAsyncGuideViewController.m
//  ReactNativeAsyncLoadBundle
//
//  Created by Marcus on 2020/3/19.
//  Copyright © 2020 Facebook. All rights reserved.
//

#import "MMAsyncGuideViewController.h"
#import "MMAsyncReactNativeContainerViewController.h"
#import "MMAsyncLoadManager.h"
#import "MMTimeRecordUtil.h"
#import "Masonry.h"

@interface MMAsyncGuideViewController ()

@end

@implementation MMAsyncGuideViewController

- (void)viewDidLoad {
  [super viewDidLoad];
  // Do any additional setup after loading the view.
  [self.navigationItem setTitle:@"Async Guide"];
  [self initCustomViews];
  [[MMAsyncLoadManager getInstance] prepareReactNativeEnvWithCommonBundleName:@"bundle/common.ios"
                                                                withExtension:@"bundle"];
}

-(void)initCustomViews{
  CGRect screenRect = [[UIScreen mainScreen] bounds];
  
  UILabel *labelTips = [[UILabel alloc] init];
  labelTips.text = @"This activity loads a common bundle file in the backgroud, it is used to simulate a PARENT activity of the activity using react native. This activity can usually be dislayed the entrance of your business which was builded by react native in your official app. You can start the activity using react native by clicking the button blew.";
  
  labelTips.font = [UIFont systemFontOfSize:18];
  labelTips.lineBreakMode = NSLineBreakByWordWrapping;
  labelTips.numberOfLines = 0;
  [self.view addSubview:labelTips];
  
  [labelTips mas_makeConstraints:^(MASConstraintMaker *make) {
     make.height.equalTo(@240);
     make.left.equalTo(self.view).offset(20);
     make.right.equalTo(self.view).offset(-20);
     make.centerY.equalTo(self.view.mas_top).offset(screenRect.size.height/3);
     make.centerX.mas_equalTo(self.view);
   }];
  
  UIButton *btnStart = [[UIButton alloc] init];
  [btnStart setTitle:@"Start" forState:UIControlStateNormal];
  [btnStart setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
  [btnStart setBackgroundColor:[UIColor lightGrayColor]];
  [btnStart setTag: 3];
  [btnStart addTarget:self
              action:@selector(btnClickListener:)
    forControlEvents:UIControlEventTouchUpInside];
  [self.view addSubview:btnStart];
  
  [btnStart mas_makeConstraints:^(MASConstraintMaker *make) {
    make.left.equalTo(self.view).offset(20);
    make.right.equalTo(self.view).offset(-20);
    make.height.equalTo(@60);
    make.centerY.equalTo(self.view.mas_top).offset(screenRect.size.height/3*2);
    make.centerX.equalTo(self.view);
  }];
}

-(void)btnClickListener:(UIButton *) button{
  if(button.tag == 3){
    NSLog(@"Start Button Clicked.");
    MMAsyncReactNativeContainerViewController *controller = [[MMAsyncReactNativeContainerViewController alloc] init];
    [self.navigationController pushViewController:controller animated:NO];
    [[MMTimeRecordUtil getInstance] setStartTime:@"ViewAction"];
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
