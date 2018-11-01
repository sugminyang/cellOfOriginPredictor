#install.packages("xgboost")
#install.packages("dplyr")
library(xgboost)
library("optparse")

option_list = list(
  make_option(c("-s", "--source"), type="character", default="/Users/dean/Downloads/project/dummy", 
              help="dataset file name", metavar="character"),
  make_option(c("-o", "--out"), type="character", default="/Users/dean/Downloads/project/result", 
              help="output file name [default= %default]", metavar="character")
); 

opt_parser = OptionParser(option_list=option_list);
opt = parse_args(opt_parser);

#define serveral paths
#------------------------------------------------------------------
sourcePath <- paste0(opt$source,"/")
resultPath <- paste0(opt$out,"/")
#------------------------------------------------------------------

#read epimarker_rawdata & mutation_rawdata
#------------------------------------------------------------------
epimarker_rawdata <- paste0(sourcePath,'rawfs673.csv')
bio = read.csv(epimarker_rawdata)
mutation_rawdata <- paste0(sourcePath,'mutation.csv')
mutation = read.csv(mutation_rawdata)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
#------------------------------------------------------------------

#set repeat count. 
#------------------------------------------------------------------
nround=5
#------------------------------------------------------------------
rp = 2
mEta = 0.5
obj = "multi:softmax"
eval_met = "merror"
msubsample = 0.9
mgamma = 0

#rotation all mutations
#------------------------------------------------------------------
for(mutaionIndex in 1:ncol(mutation))  
{
  progSt <- proc.time()
  #repeat 100 to calculate average of epimarker's rank
  #------------------------------------------------------------------
  for(repeatCount in 1:rp)  
  {
    #split training & test data (9:1)
    #------------------------------------------------------------------
    ind <- sample(2, nrow(bio), replace = TRUE, prob = c(0.9, 0.1))
    train = bio[ind==1,]
#    #colnames(train)
    test = bio[ind==2,]
    #colnames(test)
    #------------------------------------------------------------------
    
    #check the name of mutation on the console.
    print(colnames(mutation[mutaionIndex]))
    
    #set label data
    #------------------------------------------------------------------
    train_y = mutation[ind==1,c(mutaionIndex)]
    mutationName <- colnames(mutation[c(mutaionIndex)])
    
    #print(mutation[ind==1,c(mutaionIndex)])
    test_y = mutation[ind==2,c(mutaionIndex)]
    #------------------------------------------------------------------
    
    num_class <- as.integer(max(mutation[mutaionIndex])) +1
    
    #set general parameters
    #------------------------------------------------------------------
    params <- list(booster = "gbtree",objective = obj, num_class=num_class, eval_metric = eval_met, num_feature=ncol(bio))
    #------------------------------------------------------------------
    
    #make adequate data format to use in XGBoost library..
    #------------------------------------------------------------------
    dtrain = xgb.DMatrix(as.matrix(train),label=train_y)
    dtest= xgb.DMatrix(as.matrix(test),label=test_y)
    #------------------------------------------------------------------
    
    watchlist=list(train=dtrain,test=dtest)
    
    #check excution time
    ptm <- proc.time()
    
    bst = xgb.train(data = dtrain,watchlist = watchlist, params = params, nrounds = nround,verbose = 1, eta = mEta, gamma = mgamma)
    print(repeatCount);print(proc.time() - ptm)
    
    
    #get importance from model.
    #------------------------------------------------------------------
    importance <- xgb.importance(feature_names = colnames(train),model=bst)
    #head(importance, n = 20)
    #------------------------------------------------------------------
    
    #save epimarker's importance TOP20
    #------------------------------------------------------------------
    capture.output(print(bst$evaluation_log),file= paste0(resultPath,'evalLog_',mutationName,'.txt'),append=TRUE)
    #capture.output(head(importance, n = 20),file= paste0(resultPath,'top20Import_',mutationName,'.txt'),append=TRUE)
    #------------------------------------------------------------------
    
    #saving the ordered epimarkers(rank)
    #------------------------------------------------------------------
    for(i in 1: nrow(importance)) 
    {
     capture.output(cat(' ',importance[i]$Feature),file= paste0(resultPath,'rank_',mutationName,'.txt'),append=TRUE)
    }
    #------------------------------------------------------------------
    
    #new line
    capture.output(cat('\n'),file= paste0(resultPath,'rank_',mutationName,'.txt'),append=TRUE)
  }
  #------------------------------------------------------------------
  
  #excute external .jar file.
  # java -jar filePath arg1 arg2 arg3 (arg1:resultpath, args2:mutationName, args3:top Count)
  #------------------------------------------------------------------
  jarPath <- paste0(sourcePath,"ranker.jar")
  excuteJar <- paste("java -jar",jarPath,resultPath,colnames(mutation[mutaionIndex]),20,"t")
  javaOutput <- system(excuteJar, intern = TRUE)
  #------------------------------------------------------------------
  
  capture.output(cat(proc.time() - progSt),file= paste0(resultPath,'time_',mutationName,rp,'rp.txt'),append=TRUE)
}



