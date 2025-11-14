package pl.joboffers;

public interface WireMockJobOffersResponse {

    default String retrieveFourOffersJson(){
        return                                 """
                                        [
                                          {
                                            "title": "Java Developer",
                                            "company": "Tech Solutions",
                                            "salary": "7000 - 9000",
                                            "offerUrl": "https://techsolutions.com/jobs/java-developer"
                                          },
                                          {
                                            "title": "Backend Engineer (Java)",
                                            "company": "InnovateX",
                                            "salary": "7000 - 9000",
                                            "offerUrl": "https://innovatex.com/careers/backend-java"
                                          },
                                          {
                                            "title": "Junior Java Developer",
                                            "company": "CloudWorks",
                                            "salary": "7000 - 9000",
                                            "offerUrl": "https://cloudworks.io/jobs/junior-java"
                                          },
                                          {
                                            "title": "Java Software Engineer",
                                            "company": "NextGen Apps",
                                            "salary": "7000 - 9000",
                                            "offerUrl": "https://nextgenapps.com/jobs/java-software-engineer"
                                          }
                                        ]
                                        
                                        """.trim();
    }

    default String retrieveTwoOffersJson(){
            return                                 """
                                        [
                                          {
                                            "title": "Java Developer",
                                            "company": "Tech Solutions",
                                            "salary": "7000 - 9000",
                                            "offerUrl": "https://techsolutions.com/jobs/java-developer"
                                          },
                                          {
                                            "title": "Backend Engineer (Java)",
                                            "company": "InnovateX",
                                            "salary": "7000 - 9000",
                                            "offerUrl": "https://innovatex.com/careers/backend-java"
                                          }
                                      
                                        ]
                                        
                                        """.trim();

    }

    default String retrieveOneOfferJson(){
        return                   """
                                        [
                                          {
                                            "title": "Java Developer",
                                            "company": "Tech Solutions",
                                            "salary": "7000 - 9000",
                                            "offerUrl": "https://techsolutions.com/jobs/java-developer"
                                          }
                                      
                                        ]
                                        
                                        """.trim();
    }

    default String retrieveZeroOffersJson(){
        return "[]";
    }



}
