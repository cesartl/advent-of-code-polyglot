import System.IO

main :: IO()
main = do
  handle <- openFile "input/day2.txt" ReadMode  
  contents <- hGetContents handle
  instructions <- pure $ lines contents
  putStrLn $ show (solve instructions)

data Direction = U | L | R | D
  deriving (Eq, Show)

parseDirection :: Char -> Maybe Direction
parseDirection 'U' = Just U
parseDirection 'D' = Just D
parseDirection 'L' = Just L
parseDirection 'R' = Just R
parseDirection _ = Nothing

parseLine :: String -> Maybe [Direction]
parseLine = traverse parseDirection

move :: Int -> Direction -> Int
move 1 R = 2
move 1 D = 4
move 1 _ = 1

move 2 R = 3
move 2 L = 1
move 2 D = 5
move 2 _ = 2

move 3 L = 2
move 3 D = 6
move 3 _ = 3

move 4 U = 1
move 4 R = 5
move 4 D = 7
move 4 _ = 4

move 5 U = 2
move 5 R = 6
move 5 L = 4
move 5 D = 8

move 6 L = 5
move 6 U = 3
move 6 D = 9
move 6 _ = 6

move 7 U = 4
move 7 R = 8
move 7 _ = 7

move 8 L = 7
move 8 U = 5
move 8 R = 9
move 8 _ = 8

move 9 U = 6
move 9 L = 8
move 9 _ = 9

move x _ = x

reduceDirection :: Int -> [Direction] -> Int
reduceDirection start = foldl move start

processLine :: String -> Maybe (Int -> Int)
processLine s = do
    directions <- parseLine s
    pure $ \start -> reduceDirection start directions

findCode :: [String] -> Int -> Maybe [Int]
findCode instructions = do
  handlers <- traverse processLine instructions
  pure $ foldl f [] handlers
  where
    f :: [Int] -> (Int -> Int) -> [Int]
    f = ...


solve :: [String] -> Maybe [Int]
solve instructions = findCode instructions 5