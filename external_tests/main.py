import re
import unittest
import requests
import json

HEADERS = {
    "Content-Type": "application/json",
    "Accept": "application/json"
}

BASE_URL = "http://localhost:8080/"

class TestBlog(unittest.TestCase):

    def setUp(self) -> None:
        self.blog_url = BASE_URL + "blog"

    def test_get(self):
        ret = requests.get(self.blog_url)

        self.assertEqual(200, ret.status_code)

    def test_put(self):
        blog_data = {
            "name": "Superduper",
            "description": "Lol how nice",
            "logoUrl": ""
        }

        ret = requests.put(self.blog_url, data=json.dumps(blog_data), headers=HEADERS)

        self.assertEqual(204, ret.status_code)

        ret = requests.get(self.blog_url)

        self.assertEqual("Superduper", json.loads(ret.content)["name"])

class TestPosts(unittest.TestCase):

    def setUp(self) -> None:
        self.post_url = BASE_URL + "blog/posts"

    def test_get_list(self):
        ret = requests.get(self.post_url)

        self.assertEqual(200, ret.status_code)

if __name__ == '__main__':
    unittest.main()